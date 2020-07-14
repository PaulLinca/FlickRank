import requests
from bs4 import BeautifulSoup
from crawler import constants
from crawler.review import Review, MovieReviewData


def getMovieReviews(imdbId):
    # movieTitle, overallRating = getMoviePageData(imdbId)

    return getReviewPageData(imdbId)


def getMoviePageData(imdbId):
    page = requests.get(constants.imdbMovieBaseUrl + imdbId).text
    soup = BeautifulSoup(page, 'lxml')

    #Title
    title = soup.find('div', class_='title_wrapper').h1.text

    #Overall rating
    overallRating = soup.find('div', class_='titleReviewBarItem').a.div.span.text

    return title, overallRating

def getReviewPageData(imdbId):
    try:
        page = requests.get(constants.imdbMovieBaseUrl + imdbId + '/criticreviews?ref_=tt_ov_rt').text
        soup = BeautifulSoup(page, 'lxml')

        # Need this header cause site detects the script
        header = {'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.0 Safari/537.36'}
        linkToMetacriticPage = soup.find('div', class_='see-more').a['href']
        metacriticPage = requests.get(linkToMetacriticPage, headers=header).text
        soup = BeautifulSoup(metacriticPage, 'lxml')

        criticReviewsContainers = soup.find('div', class_='critic_reviews2').findAll('div', class_='review pad_top2 pad_btm2')
        # userReviewsContainers = soup.find('div', class_='user_reviews2')

        return getReviews(criticReviewsContainers)
    except:
        return []

def getReviews(reviewContainers):
    allReviews = []
    for reviewDiv in reviewContainers:
        # Get rating
        rating = reviewDiv.find('div', class_='score_wrap').div.text

        # Get title
        titleContainer = reviewDiv.find('div', class_='pub_wrap title')
        source = titleContainer.find('span', class_='source').a.text.strip()
        if len(source) == 0:
            source = titleContainer.find('span', class_='source').a.img['title']
        author = titleContainer.find('span', class_='author').a
        if author is not None:
            author = author.text.strip()
        else:
            author = titleContainer.find('span', class_='author').text.strip()

        title = source + " by author " + author

        # Get review text
        reviewTextContainer = reviewDiv.find('div', class_='summary')
        textContainer = reviewTextContainer.find('a', class_='no_hover')

        if textContainer is not None:
            text = textContainer.text.strip()
        else:
            text = reviewTextContainer.text.strip()

        movieReview = Review('metacritic', rating, title, text, False)
        allReviews.append(movieReview)

    return allReviews
