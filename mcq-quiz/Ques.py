import random
import uuid
from typing import List, Literal, Optional,Dict
 
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel, Field
from langchain_core.prompts import ChatPromptTemplate
from langchain_groq import ChatGroq

from contextlib import asynccontextmanager
import py_eureka_client.eureka_client as eureka_client
 
load_dotenv()
 
app = FastAPI(title="MCQ Quiz API")
llm = ChatGroq(model="openai/gpt-oss-120b", temperature=0.5)
 
LETTERS = ["A", "B", "C", "D"]
 
 
# ---------- LLM structure ----------
class MCQ(BaseModel):
    question: str = Field(description="The question text")
    options: List[str] = Field(
        min_length=4, max_length=4,
        description="Exactly 4 answer options, without letter labels like 'A)'"
    )
    correct_index: int = Field(
        ge=0, le=3,
        description="Index (0-3) of the correct option in the options list"
    )
    explanation: str = Field(description="One or two sentences explaining why the answer is correct")
 
 
class MCQSet(BaseModel):
    items: List[MCQ]
 
 
# ---------- Prompt ----------
quiz_prompt = ChatPromptTemplate.from_messages([
    ("system", "You are an expert teacher who writes clear multiple choice questions."),
    ("human", "Generate exactly {num_questions} {difficulty} level multiple choice questions "
              "covering these topics: {topics}. "
              "Spread the questions as evenly as possible across all the topics. "
              "Each question must have exactly 4 options with exactly one correct answer. "
              "Make the wrong options plausible but clearly incorrect.")
])
quiz_chain = quiz_prompt | llm.with_structured_output(MCQSet)
 
 
class StoredQuestion(BaseModel):
    question: str
    options: List[str]
    correct_letter: str
    explanation: str
 
 
quizzes: Dict[str, List[StoredQuestion]] = {}
 
 
# ---------- API models ----------
class QuizRequest(BaseModel):
    topics: List[str] = Field(min_length=1, description="One or more topics for the test")
    num_questions: int = Field(ge=1, le=20)
    difficulty: Literal["easy", "medium", "hard"]
 
 
class QuestionOut(BaseModel):
    number: int
    question: str
    options: dict[str, str]
 
 
class QuizResponse(BaseModel):
    quiz_id: str
    questions: List[QuestionOut]
 
 
class AnswerRequest(BaseModel):
    option: str = Field(pattern="^[A-Da-d]$", description="Your chosen option: A, B, C or D")
 
 
class AnswerResponse(BaseModel):
    is_correct: bool
    your_option: str
    correct_option: Optional[str] = None
    correct_answer: Optional[str] = None
    explanation: str
 
 
# ---------- Endpoints ----------
@app.post("/quiz", response_model=QuizResponse)
def create_quiz(req: QuizRequest):
    topics = [t.strip() for t in req.topics if t.strip()]
    if not topics:
        raise HTTPException(status_code=422, detail="At least one non-empty topic is required")
 
    try:
        mcqs = quiz_chain.invoke({
            "topics": ", ".join(topics),
            "num_questions": req.num_questions,
            "difficulty": req.difficulty,
        })
    except Exception as e:
        raise HTTPException(status_code=502, detail=f"LLM error: {e}")
 
    stored: List[StoredQuestion] = []
    for m in mcqs.items[: req.num_questions]:
        correct_text = m.options[m.correct_index]
        shuffled = m.options[:]
        random.shuffle(shuffled)  # LLMs favour certain positions, so shuffle
        stored.append(StoredQuestion(
            question=m.question,
            options=shuffled,
            correct_letter=LETTERS[shuffled.index(correct_text)],
            explanation=m.explanation,
        ))
 
    quiz_id = str(uuid.uuid4())
    quizzes[quiz_id] = stored
 
    return QuizResponse(
        quiz_id=quiz_id,
        questions=[
            QuestionOut(number=i, question=q.question, options=dict(zip(LETTERS, q.options)))
            for i, q in enumerate(stored, 1)
        ],
    )
 
 
@app.post("/quiz/{quiz_id}/answer/{number}", response_model=AnswerResponse)
def check_answer(quiz_id: str, number: int, req: AnswerRequest):
    quiz = quizzes.get(quiz_id)
    if quiz is None:
        raise HTTPException(status_code=404, detail="Quiz not found or expired")
    if not 1 <= number <= len(quiz):
        raise HTTPException(status_code=404, detail="Question number out of range")

    q = quiz[number - 1]
    chosen = req.option.upper()
    is_correct = chosen == q.correct_letter
 
    return AnswerResponse(
        is_correct=is_correct,
        your_option=chosen,
        correct_option=None if is_correct else q.correct_letter,
        correct_answer=None if is_correct else q.options[LETTERS.index(q.correct_letter)],
        explanation=q.explanation,
    )
 
 
if __name__ == "__main__":
    import uvicorn
    # "Ques" must match this file's name (Ques.py)
    uvicorn.run("Ques:app", host="127.0.0.1", port=8085, reload=True)

@asynccontextmanager
async def lifespan(app: FastAPI):
    await eureka_client.init_async(
        eureka_server="http://localhost:8081/eureka",
        app_name="quiz-api",
        instance_port=8085,
        instance_host="127.0.0.1",
        health_check_url="http://127.0.0.1:8085/health",
    )
    yield
    await eureka_client.stop_async()

app = FastAPI(title="MCQ Quiz API", lifespan=lifespan)

@app.get("/health")
def health():
    return {"status": "UP"}