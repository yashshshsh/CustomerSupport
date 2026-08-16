import joblib


# Load trained model
model = joblib.load("models/ticket_classifier.joblib")

# Load TF-IDF vectorizer
vectorizer = joblib.load("models/tfidf_vectorizer.joblib")


def predict_category(ticket_text: str):

    ticket_vectorized = vectorizer.transform([ticket_text])

    # Predict category
    prediction = model.predict(ticket_vectorized)[0]

    # Get probability for each category
    probabilities = model.predict_proba(ticket_vectorized)[0]

    # Get probability of predicted category
    predicted_index = list(model.classes_).index(prediction)

    confidence = probabilities[predicted_index]

    return {
        "category": prediction,
        "confidence": round(float(confidence), 4)
    }