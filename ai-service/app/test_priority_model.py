import joblib


# ============================================================
# LOAD PRIORITY MODEL
# ============================================================

model = joblib.load(
    "models/priority_classifier.joblib"
)

vectorizer = joblib.load(
    "models/priority_tfidf_vectorizer.joblib"
)


# ============================================================
# UNSEEN TEST CASES
# ============================================================

test_cases = [

    {
        "text": "I've lost control of my account and somebody is changing things.",
        "expected": "HIGH"
    },

    {
        "text": "I don't recognize a payment that appeared this morning.",
        "expected": "HIGH"
    },

    {
        "text": "Someone seems to have gained access to my account.",
        "expected": "HIGH"
    },

    {
        "text": "My money was taken but the purchase did not go through.",
        "expected": "HIGH"
    },

    {
        "text": "Could you tell me what features come with the basic subscription?",
        "expected": "LOW"
    },

    {
        "text": "I would like to update the personal details on my account.",
        "expected": "LOW"
    },

    {
        "text": "What options are available for changing my membership?",
        "expected": "LOW"
    },

    {
        "text": "Can you explain which payment methods are supported?",
        "expected": "LOW"
    },

    {
        "text": "My delivery hasn't arrived even though the expected date passed.",
        "expected": "MEDIUM"
    },

    {
        "text": "The application keeps displaying an error when I submit the form.",
        "expected": "MEDIUM"
    },

    {
        "text": "My order is taking longer than the estimated delivery time.",
        "expected": "MEDIUM"
    },

    {
        "text": "The website stops responding whenever I try to complete my order.",
        "expected": "MEDIUM"
    },

    {
        "text": "I cannot get the application to open properly.",
        "expected": "MEDIUM"
    },

    {
        "text": "There is an unauthorized transaction on my card.",
        "expected": "HIGH"
    },

    {
        "text": "How can I modify my contact information?",
        "expected": "LOW"
    }
]


# ============================================================
# RUN TESTS
# ============================================================

correct = 0

print("\n==============================================")
print("       PRIORITY UNSEEN TEST")
print("==============================================\n")


for index, test in enumerate(test_cases, start=1):

    text = test["text"]
    expected = test["expected"]

    vectorized_text = vectorizer.transform([text])

    prediction = model.predict(
        vectorized_text
    )[0]

    probabilities = model.predict_proba(
        vectorized_text
    )[0]

    predicted_index = list(
        model.classes_
    ).index(prediction)

    confidence = probabilities[
        predicted_index
    ]

    is_correct = prediction == expected

    if is_correct:
        correct += 1

    print(f"Test {index}")
    print(f"Text       : {text}")
    print(f"Expected   : {expected}")
    print(f"Predicted  : {prediction}")
    print(f"Confidence : {confidence:.4f}")
    print(
        f"Result     : "
        f"{'PASS' if is_correct else 'FAIL'}"
    )
    print("----------------------------------------------")


# ============================================================
# SUMMARY
# ============================================================

total = len(test_cases)

accuracy = correct / total

print("\n==============================================")
print("                SUMMARY")
print("==============================================")

print(f"Total tests : {total}")
print(f"Correct     : {correct}")
print(f"Incorrect   : {total - correct}")
print(f"Accuracy    : {accuracy:.2%}")

print("==============================================\n")