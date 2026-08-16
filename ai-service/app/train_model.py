import os
import pandas as pd
import joblib

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report


# Load dataset
df = pd.read_csv("data/tickets.csv")

X = df["text"]
y = df["category"]


# Split data
X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.25,
    random_state=42,
    stratify=y
)


# TF-IDF
vectorizer = TfidfVectorizer(
    lowercase=True,
    strip_accents="unicode",
    ngram_range=(1, 2),
    min_df=1,
    sublinear_tf=True
)

X_train_vectorized = vectorizer.fit_transform(X_train)
X_test_vectorized = vectorizer.transform(X_test)


# Model
model = LogisticRegression(
    C=2.0,
    max_iter=2000,
    class_weight="balanced"
)

model.fit(X_train_vectorized, y_train)


# Evaluate
y_pred = model.predict(X_test_vectorized)

accuracy = accuracy_score(y_test, y_pred)

print("\n========== MODEL EVALUATION ==========")
print(f"Accuracy: {accuracy:.2f}")
print("\nClassification Report:")
print(classification_report(y_test, y_pred))
print("======================================\n")


# Create models directory
os.makedirs("models", exist_ok=True)


# Save model and vectorizer
joblib.dump(model, "models/ticket_classifier.joblib")
joblib.dump(vectorizer, "models/tfidf_vectorizer.joblib")

print("Model saved successfully.")