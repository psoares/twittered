package com.github.redouane59.twitter.nrt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.redouane59.RelationType;
import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.others.RequestToken;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ITwitterClientV1Test {

  private static TwitterClient twitterClient;

  @BeforeAll
  public static void init() throws IOException {
    String credentialPath = "C:/Users/Perso/Documents/GitHub/twitter-credentials.json";
    twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                          .readValue(new File(credentialPath), TwitterCredentials.class));
  }

  @Test
  public void testGetFollowingIdsById() {
    List<String> followings = twitterClient.getFollowingIds("882266619115864066");
    assertTrue(followings.size() > 200);
  }

  @Test
  public void testGetFollowersIdsById() {
    List<String> followers = twitterClient.getFollowerIds("882266619115864066");
    assertTrue(followers.size() > 200);
  }

  @Test
  public void testGetFollowersUsersById() {
    List<User> followers = twitterClient.getFollowerUsers("882266619115864066");
    assertTrue(followers.size() > 200);
  }

  @Test
  public void testFriendshipByIdYes() {
    String       userId1 = "92073489";
    String       userId2 = "723996356";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertEquals(RelationType.FRIENDS, result);
  }

  @Test
  public void testFriendshipByIdNo() {
    String       userId1 = "92073489";
    String       userId2 = "1976143068";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertNotEquals(RelationType.FRIENDS, result);
  }

  @Test
  public void testGetRateLimitStatus() {
    assertNotEquals(null, twitterClient.getRateLimitStatus());
  }

  @Test
  public void testRelationBetweenUsersIdFriends() {
    String       userId1 = "92073489";
    String       userId2 = "723996356";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertEquals(RelationType.FRIENDS, result);
  }

  @Test
  public void testRelationBetweenUsersIdNone() {
    String       userId1 = "92073489";
    String       userId2 = "1976143068";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertEquals(RelationType.NONE, result);
  }

  @Test
  public void testRelationBetweenUsersIdFollowing() {
    String       userId1 = "92073489";
    String       userId2 = "126267113";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertEquals(RelationType.FOLLOWING, result);
  }

  @Test
  public void testRelationBetweenUsersIdFollower() {
    String       userId1 = "92073489";
    String       userId2 = "1218125226095054848";
    RelationType result  = twitterClient.getRelationType(userId1, userId2);
    assertEquals(RelationType.FOLLOWER, result);
  }

  @Test
  public void testGetRetweetersId() {
    String tweetId = "1078358350000205824";
    assertTrue(twitterClient.getRetweetersId(tweetId).size() > 10);
  }

  @Test
  public void testGetLastTweetByUserName() {
    String      userName = "RedTheOne";
    List<Tweet> response = twitterClient.getUserLastTweets(userName, 2);
    assertTrue(response.get(0).getLang().equals("fr")
               || response.get(1).getLang().equals("fr"));
  }

  @Test
  public void testGetLastTweetByUserId() {
    String      userId   = "92073489";
    List<Tweet> response = twitterClient.getUserLastTweets(userId, 3);
    assertTrue(response.get(0).getLang().equals("fr")
               || response.get(1).getLang().equals("fr"));
  }

  @Test
  public void testGetOauth1Token() {
    TwitterClient.TWITTER_CREDENTIALS.setAccessToken("");
    TwitterClient.TWITTER_CREDENTIALS.setAccessTokenSecret("");
    RequestToken result = twitterClient.getOauth1Token();
    assertTrue(result.getOauthToken().length() > 1);
    assertTrue(result.getOauthTokenSecret().length() > 1);
  }

  @Test
  public void testFollowAndUnfollow() {
    User user         = twitterClient.getUserFromUserName("red1");
    User followedUser = twitterClient.follow(user.getId());
    assertEquals("red1", followedUser.getName());
    User unfollowedUser = twitterClient.unfollow(user.getId());
    assertEquals("red1", unfollowedUser.getName());
    assertEquals(RelationType.NONE, twitterClient.getRelationType("92073489", "66533"));
  }

  @Test
  public void testLikeTweet() {
    Tweet likedTweet = twitterClient.likeTweet("1107533");
    assertEquals("1107533", likedTweet.getId());
    Tweet unlikedTweet = twitterClient.unlikeTweet("1107533");
    assertEquals("1107533", unlikedTweet.getId());
  }

  @Test
  public void testPostAndRTandDeleteTweet() {
    String text       = "API Test " + LocalDateTime.now() + " #TwitterAPI";
    Tweet  resultPost = twitterClient.postTweet(text);
    assertNotNull(resultPost);
    assertNotNull(resultPost.getId());
    assertEquals(text, resultPost.getText());
    Tweet resultRT = twitterClient.retweetTweet(resultPost.getId());
    assertNotNull(resultRT);
    assertNotNull(resultRT.getId());
    assertEquals(resultPost.getAuthorId(), resultRT.getAuthorId());
    Tweet resultDelete = twitterClient.deleteTweet(resultPost.getId());
    assertNotNull(resultDelete);
    assertEquals(resultPost.getId(), resultDelete.getId());
  }

  @Test
  public void testGetFavorites() {
    int         count     = 1500;
    List<Tweet> favorites = twitterClient.getFavorites("92073489", count);
    assertNotNull(favorites);
    assertTrue(favorites.size() > count);
  }

  @Test
  public void testSearchTweets30days() {
    List<Tweet>
        result =
        twitterClient.searchForTweetsWithin30days("@RedTheOne -RT", LocalDateTime.of(2020, 9, 1, 0, 0), LocalDateTime.of(2020, 9, 3, 0, 0), "30days");
    assertTrue(result.size() > 0);
  }

        /*

    @Test
    public void testSearchTweetsArchive(){
        LocalDateTime startDate = DateUtils.truncate(ConverterHelper.dayBeforeNow(60),Calendar.MONTH);
        LocalDateTime endDate = DateUtils.addDays(startDate, 1);
        List<ITweet> result = twitterClient.searchForTweetsArchive("@RedTheOne -RT",startDate, endDate);
        assertTrue(result.size()>0);
    } */

}