package fr.dr.rss;

/**
 * TODO.
 * User: drieu
 * Date: 15/11/13
 * Time: 14:08
 */
public class RssReader {


    public static void main(String[] args) {
        RSSFeedParser parser = new RSSFeedParser("http://www.vogella.com/article.rss");
        Feed feed = parser.readFeed();
        System.out.println(feed);
        for (FeedMessage message : feed.getMessages()) {
            System.out.println(message);

        }
    }
}
