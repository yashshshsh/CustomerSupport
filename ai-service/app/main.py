from fastapi import FastAPI
from pydantic import BaseModel

from app.ml_model import (
    predict_category,
    predict_priority
)


app = FastAPI(
    title="Customer Support AI Service",
    description="AI service for ticket category and priority prediction",
    version="1.0.0"
)


# ============================================================
# REQUEST MODEL
# ============================================================

class TicketRequest(BaseModel):
    text: str


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