import csv
import random

random.seed(42)

priority_examples = {

    "HIGH": [
        "My account has been hacked",
        "Someone accessed my account without permission",
        "I think someone has stolen my account",
        "My account security has been compromised",
        "I cannot access my account after someone changed my password",
        "Someone logged into my account",
        "My account was accessed by someone else",
        "I believe my account has been compromised",
        "Someone is using my account",
        "My account has been taken over",
        "I cannot access my account and I think it was hacked",
        "There is unauthorized access to my account",
        "My account was hacked and I need help immediately",
        "Someone changed my account password",
        "I received an unauthorized login alert",
        "My account is being used by someone else",
        "I lost access to my account because of suspicious activity",
        "I think someone stole my credentials",
        "There is suspicious activity on my account",
        "My account has unauthorized activity",

        "Someone changed my personal details without my permission.",
        "My account information was changed by someone else.",
        "Someone modified my profile without my knowledge.",
        "My account details were changed unexpectedly.",
        "Someone changed my phone number on my account.",
        "My email address was changed without my permission.",
        "Someone changed my profile information.",
        "My account details were modified by an unknown person.",
        "I did not make the changes to my account information.",
        "Someone has changed my account settings without my permission.",
        "My personal information was changed by someone else.",
        "I noticed unauthorized changes to my profile.",
        "Someone changed my contact information without asking me.",
        "My account profile has been modified without my knowledge.",
        "There are changes to my account that I did not make.",

        "I was charged twice for the same transaction",
        "I was charged multiple times",
        "My card was charged twice",
        "There are duplicate charges on my account",
        "I see an unauthorized charge on my card",
        "I don't recognize this transaction",
        "Someone made a payment using my card",
        "There is a fraudulent transaction on my account",
        "My card was used without my permission",
        "I noticed an unauthorized payment",

        "I cannot access my account",
        "I am completely locked out of my account",
        "My account is locked and I cannot log in",
        "I urgently need access to my account",
        "I have been locked out of my account",
        "I cannot sign in despite using the correct password",
        "My account access has been blocked",
        "I urgently need to recover my account",

        "My payment was taken but my order was not created",
        "Money was deducted but the transaction failed",
        "My money was deducted and I did not receive the service",
        "I was charged but my purchase failed",
        "The payment was completed but my order is missing",
    ],

    "MEDIUM": [
        "My package is delayed",
        "My parcel has not arrived",
        "My shipment is late",
        "The delivery is taking too long",
        "My order has not arrived yet",
        "The package delivery is delayed",
        "I am still waiting for my package",
        "My shipment is taking longer than expected",
        "The delivery has been delayed",

        "The application is showing an error",
        "The website is not working properly",
        "The application keeps crashing",
        "The system is not responding",
        "The website stopped working",
        "The app is not opening",
        "I am having a technical problem",
        "The application is running into an error",
        "The website keeps showing an error",

        "My payment failed",
        "My card payment did not go through",
        "The transaction failed",
        "I cannot complete my payment",
        "My payment was declined",
        "The checkout payment failed",
        "I am having trouble making a payment",
        "My credit card payment failed",
        "The payment keeps failing",

        "I received the wrong item",
        "The item in my order is incorrect",
        "My order contains the wrong product",
        "I received a different product",
        "There is a problem with my order",
        "My order details are incorrect",
        "The product I received is not what I ordered",

        "I am having trouble logging in",
        "The login page is not working",
        "The sign in page keeps failing",
        "I cannot sign in",
        "My login attempt is failing",
        "The login page shows an error",

        "My refund has not arrived",
        "I am still waiting for my refund",
        "My refund is delayed",
        "The refunded amount has not reached me",
        "I have not received my refund yet",
    ],

    "LOW": [
        "How do I change my email address?",
        "How can I update my profile?",
        "How do I change my account information?",
        "Can I update my phone number?",
        "How can I edit my personal information?",
        "How do I modify my profile?",
        "Can I change my account details?",
        "How can I update my email?",
        "How do I correct my account information?",

        "How much does the subscription cost?",
        "What are your subscription plans?",
        "How much is the premium plan?",
        "What plans do you offer?",
        "Can you tell me about the subscription?",
        "What is included in the membership?",
        "How can I upgrade my subscription?",
        "How can I downgrade my plan?",
        "What subscription options are available?",

        "How can I update my personal information?",
        "I want to change the details on my account.",
        "How do I edit my profile information?",
        "I need to update my account details.",
        "Can I change the information in my profile?",
        "How do I modify my personal information?",
        "I want to change my contact details.",
        "How can I edit my account information?",
        "I need to update my profile details.",
        "Can I change my phone number?",
        "How do I update my address?",
        "How can I change my contact information?",
        "I want to edit my account profile.",
        "Can I update my personal details?",
        "How do I modify my profile?",
        "I want to change my account details.",
        "How can I update my account information?",
        "Can I edit my personal details?",
        "I need to change my profile information.",
        "How do I update my contact details?",

        "How does the service work?",
        "Can you explain the platform?",
        "I want to learn about the service",
        "How does your customer service work?",
        "Can you provide information about the platform?",
        "What features does the service provide?",
        "I would like information about the service",

        "How do I change my password?",
        "How can I reset my password?",
        "I forgot my password, how can I change it?",
        "How do I update my password?",
        "Can you tell me how to reset my password?",

        "What payment methods do you accept?",
        "Which cards can I use?",
        "Can I pay using a credit card?",
        "What payment options are available?",
        "Do you accept debit cards?",

        "How can I track my order?",
        "Where can I see my order?",
        "How do I track my package?",
        "Can I check my shipment status?",
        "Where can I find my delivery information?",

        "How do I cancel my subscription?",
        "Can I cancel my plan?",
        "How can I change my membership?",
        "Can I modify my service plan?",
    ]
}


def create_dataset(target_per_class=350):

    rows = []

    for priority, examples in priority_examples.items():

        generated = []

        while len(generated) < target_per_class:

            text = random.choice(examples)

            # Add small natural variations
            variations = [
                text,
                f"Please help me. {text}",
                f"Can you help me? {text}",
                f"I need help. {text}",
                f"Please tell me: {text}",
            ]

            generated.append(random.choice(variations))

        for text in generated[:target_per_class]:
            rows.append({
                "text": text,
                "priority": priority
            })

    random.shuffle(rows)

    with open(
        "data/priority_tickets.csv",
        "w",
        newline="",
        encoding="utf-8"
    ) as file:

        writer = csv.DictWriter(
            file,
            fieldnames=["text", "priority"]
        )

        writer.writeheader()
        writer.writerows(rows)

    print("Priority dataset created successfully.")
    print(f"Total examples: {len(rows)}")

    for priority in ["LOW", "MEDIUM", "HIGH"]:
        count = sum(
            1 for row in rows
            if row["priority"] == priority
        )

        print(f"{priority}: {count}")


if __name__ == "__main__":
    create_dataset()