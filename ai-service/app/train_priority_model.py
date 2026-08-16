import os
import pandas as pd
import joblib

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, classification_report


# ============================================================
# 1. LOAD DATASET
# ============================================================

df = pd.read_csv("data/priority_tickets.csv")


# Make sure required columns exist
if "text" not in df.columns or "priority" not in df.columns:
    raise ValueError(
        "Dataset must contain 'text' and 'priority' columns."
    )


# Remove empty rows
df = df.dropna(subset=["text", "priority"])


print("\n========== DATASET INFORMATION ==========")
print(f"Total examples: {len(df)}")
print("\nPriority distribution:")
print(df["priority"].value_counts())
print("=========================================\n")


X = df["text"]
y = df["priority"]


# ============================================================
# 2. TRAIN / TEST SPLIT
# ============================================================

X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.25,
    random_state=42,
    stratify=y
)


print(f"Training examples: {len(X_train)}")
print(f"Testing examples:  {len(X_test)}")


# ============================================================
# 3. TF-IDF VECTORIZATION
# ============================================================

vectorizer = TfidfVectorizer(
    lowercase=True,
    strip_accents="unicode",
    ngram_range=(1, 2),
    min_df=1,
    sublinear_tf=True
)


X_train_vectorized = vectorizer.fit_transform(X_train)
X_test_vectorized = vectorizer.transform(X_test)


print("\n========== TF-IDF ==========")
print(f"Training matrix shape: {X_train_vectorized.shape}")
print(f"Testing matrix shape:  {X_test_vectorized.shape}")
print("============================\n")


# ============================================================
# 4. TRAIN LOGISTIC REGRESSION MODEL
# ============================================================

model = LogisticRegression(
    C=2.0,
    max_iter=2000,
    class_weight="balanced"
)


model.fit(
    X_train_vectorized,
    y_train
)


# ============================================================
# 5. EVALUATE MODEL
# ============================================================

y_pred = model.predict(X_test_vectorized)


accuracy = accuracy_score(
    y_test,
    y_pred
)


print("\n==============================================")
print("       PRIORITY MODEL EVALUATION")
print("==============================================")

print(f"\nAccuracy: {accuracy:.4f}")

print("\nClassification Report:")

print(
    classification_report(
        y_test,
        y_pred
    )
)

print("==============================================\n")


# ============================================================
# 6. CREATE MODELS DIRECTORY
# ============================================================

os.makedirs(
    "models",
    exist_ok=True
)


# ============================================================
# 7. SAVE PRIORITY MODEL
# ============================================================

joblib.dump(
    model,
    "models/priority_classifier.joblib"
)


# ============================================================
# 8. SAVE PRIORITY TF-IDF VECTORIZER
# ============================================================

joblib.dump(
    vectorizer,
    "models/priority_tfidf_vectorizer.joblib"
)


# ============================================================
# 9. SUCCESS MESSAGE
# ============================================================

print("Priority model saved successfully.")

print(
    "Model: models/priority_classifier.joblib"
)

print(
    "Vectorizer: models/priority_tfidf_vectorizer.joblib"
)