import joblib


# ============================================================
# CATEGORY MODEL
# ============================================================

category_model = joblib.load(
    "models/ticket_classifier.joblib"
)

category_vectorizer = joblib.load(
    "models/tfidf_vectorizer.joblib"
)


# ============================================================
# PRIORITY MODEL
# ============================================================

priority_model = joblib.load(
    "models/priority_classifier.joblib"
)

priority_vectorizer = joblib.load(
    "models/priority_tfidf_vectorizer.joblib"
)


# ============================================================
# CATEGORY PREDICTION
# ============================================================

def predict_category(ticket_text: str):

    ticket_vectorized = category_vectorizer.transform(
        [ticket_text]
    )

    prediction = category_model.predict(
        ticket_vectorized
    )[0]

    probabilities = category_model.predict_proba(
        ticket_vectorized
    )[0]

    predicted_index = list(
        category_model.classes_
    ).index(prediction)

    confidence = probabilities[
        predicted_index
    ]

    return {
        "category": prediction,
        "confidence": round(
            float(confidence),
            4
        )
    }


# ============================================================
# PRIORITY PREDICTION
# ============================================================

def predict_priority(ticket_text: str):

    ticket_vectorized = priority_vectorizer.transform(
        [ticket_text]
    )

    prediction = priority_model.predict(
        ticket_vectorized
    )[0]

    probabilities = priority_model.predict_proba(
        ticket_vectorized
    )[0]

    predicted_index = list(
        priority_model.classes_
    ).index(prediction)

    confidence = probabilities[
        predicted_index
    ]

    return {
        "priority": prediction,
        "confidence": round(
            float(confidence),
            4
        )
    }