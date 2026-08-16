from fastapi import FastAPI
from pydantic import BaseModel

from app.ml_model import (
    predict_category,
    predict_priority
)

from app.recommendation_model import recommend_articles


app = FastAPI(
    title="Customer Support AI Service",
    description="AI service for ticket classification, priority prediction, and knowledge article recommendation",
    version="1.1.0"
)


# ============================================================
# REQUEST MODELS
# ============================================================

class TicketRequest(BaseModel):
    text: str


class Article(BaseModel):
    id: int
    title: str
    content: str


class RecommendationRequest(BaseModel):
    text: str
    articles: list[Article]
    top_k: int = 3


# ============================================================
# HEALTH CHECK
# ============================================================

@app.get("/")
def health_check():

    return {
        "message": "Customer Support AI Service is running"
    }


# ============================================================
# CATEGORY PREDICTION
# ============================================================

@app.post("/predict")
def predict(request: TicketRequest):

    return predict_category(
        request.text
    )


# ============================================================
# PRIORITY PREDICTION
# ============================================================

@app.post("/predict-priority")
def predict_priority_endpoint(
    request: TicketRequest
):

    return predict_priority(
        request.text
    )


# ============================================================
# KNOWLEDGE ARTICLE RECOMMENDATION
# ============================================================

@app.post("/recommend-articles")
def recommend_articles_endpoint(
    request: RecommendationRequest
):

    recommendations = recommend_articles(
        ticket_text=request.text,
        articles=[
            article.model_dump()
            for article in request.articles
        ],
        top_k=request.top_k
    )

    return {
        "recommendations": recommendations
    }