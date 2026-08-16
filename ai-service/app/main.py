from fastapi import FastAPI
from pydantic import BaseModel

from app.ml_model import predict_category


app = FastAPI(
    title="Customer Support AI Service",
    description="AI/ML service for the Customer Support System",
    version="1.0.0"
)


class TicketRequest(BaseModel):
    text: str


@app.get("/")
def root():
    return {
        "message": "Customer Support AI Service is running"
    }


@app.get("/health")
def health():
    return {
        "status": "UP"
    }


@app.post("/predict")
def predict(request: TicketRequest):

    result = predict_category(request.text)

    return {
        "text": request.text,
        "category": result["category"],
        "confidence": result["confidence"]
    }