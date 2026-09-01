import os
import uvicorn
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from typing import List, Dict, Any, Optional
from dotenv import load_dotenv

from services.genie import genie_service

load_dotenv()

app = FastAPI(
    title="Campus Nexus / Campus Connect Backend",
    description="FastAPI backend powered by Databricks Genie Agent for intelligent campus connections.",
    version="1.0.0",
)

# Enable CORS for Expo / React Native / Web Clients
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# Request & Response Schemas
class AskRequest(BaseModel):
    question: str = Field(
        ...,
        description="The student's natural language problem or question.",
        example="I want to build an Edge AI project using Computer Vision and IoT. Who should I connect with?",
    )
    user_id: Optional[str] = Field(
        default="adithya-kumar",
        description="ID of the student asking the question.",
    )


class AskResponse(BaseModel):
    status: str
    question: str
    answer: str
    matches: List[Dict[str, Any]]
    conversation_id: Optional[str] = None
    source: Optional[str] = None
    genie_sql: Optional[str] = None
    suggested_questions: Optional[List[str]] = None
    thoughts: Optional[List[Dict[str, Any]]] = None


@app.get("/")
def read_root():
    return {
        "app": "Campus Nexus API",
        "status": "online",
        "genie_configured": genie_service.is_configured(),
        "endpoints": {
            "ask": "POST /api/ask",
            "health": "GET /api/health",
            "docs": "/docs",
        },
    }


@app.get("/api/health")
def health_check():
    return {
        "status": "healthy",
        "genie_connected": genie_service.is_configured(),
        "host": os.getenv("DATABRICKS_HOST", "not-set"),
    }


@app.post("/api/ask", response_model=AskResponse)
async def ask_genie(request: AskRequest):
    """
    POST /api/ask
    1. Receives natural language question from mobile app.
    2. Dispatches to Databricks Genie Agent.
    3. Returns synthesized recommendations & peer match profiles.
    """
    cleaned_q = request.question.strip() if request.question else ""
    if not cleaned_q:
        raise HTTPException(status_code=400, detail="Question cannot be empty.")

    try:
        result = await genie_service.ask_question(cleaned_q)
        return AskResponse(
            status=result.get("status", "success"),
            question=cleaned_q,
            answer=result.get("answer", "Found matching campus peers and mentors."),
            matches=result.get("matches", []),
            conversation_id=result.get("conversation_id"),
            source=result.get("source"),
            genie_sql=result.get("genie_sql"),
            suggested_questions=result.get("suggested_questions"),
            thoughts=result.get("thoughts"),
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to query Genie Agent: {str(e)}")
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Failed to query Genie Agent: {str(e)}")


if __name__ == "__main__":
    host = os.getenv("HOST", "0.0.0.0")
    port = int(os.getenv("PORT", 8000))
    print(f"Starting Campus Nexus FastAPI Backend on http://{host}:{port}")
    uvicorn.run("main:app", host=host, port=port, reload=True)
