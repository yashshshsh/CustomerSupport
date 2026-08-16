from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity


def recommend_articles(ticket_text: str, articles: list, top_k: int = 3):
    """
    Recommend the most relevant knowledge articles for a ticket.

    Parameters:
        ticket_text: Customer's ticket text
        articles: List of knowledge articles
        top_k: Number of articles to return

    Returns:
        List of recommended article IDs with similarity scores
    """

    if not ticket_text or not ticket_text.strip():
        return []

    if not articles:
        return []

    # ------------------------------------------------------------
    # BUILD DOCUMENTS
    # ------------------------------------------------------------

    documents = []

    for article in articles:

        title = article.get("title", "")
        content = article.get("content", "")

        document = f"{title} {content}"

        documents.append(document)

    # ------------------------------------------------------------
    # TF-IDF
    # ------------------------------------------------------------

    vectorizer = TfidfVectorizer(
        lowercase=True,
        stop_words="english"
    )

    article_vectors = vectorizer.fit_transform(documents)

    ticket_vector = vectorizer.transform(
        [ticket_text]
    )

    # ------------------------------------------------------------
    # COSINE SIMILARITY
    # ------------------------------------------------------------

    similarities = cosine_similarity(
        ticket_vector,
        article_vectors
    )[0]

    # ------------------------------------------------------------
    # RANK ARTICLES
    # ------------------------------------------------------------

    ranked_articles = []

    for index, score in enumerate(similarities):

        ranked_articles.append({
            "articleId": articles[index]["id"],
            "score": round(float(score), 4)
        })

    ranked_articles.sort(
        key=lambda x: x["score"],
        reverse=True
    )

    # ------------------------------------------------------------
    # TOP K
    # ------------------------------------------------------------

    return ranked_articles[:top_k]