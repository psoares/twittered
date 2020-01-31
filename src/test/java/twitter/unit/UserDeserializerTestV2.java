package twitter.unit;

import com.socialmediaraiser.core.twitter.IUser;
import com.socialmediaraiser.core.twitter.helpers.JsonHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.user.UserDTOv2;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDeserializerTestV2 {

    private  File userFile2 = new File(getClass().getClassLoader().getResource("tests/user_example_v2.json").getFile());
    private IUser userV2 = JsonHelper.OBJECT_MAPPER.readValue(userFile2, UserDTOv2.class);

    public UserDeserializerTestV2() throws IOException {
    }

    @Test
    public void testGetUserIdV2() {
        assertEquals("92073489", userV2.getId());
    }

    @Test
    public void testGetUserScreenNameV2() {
        assertEquals("RedTheOne", userV2.getName());
    }

    @Test
    public void testGetUserLocationV2() {
        assertEquals("Madrid, España", userV2.getLocation());
    }

    @Test
    public void testGetUserDescriptionV2() {
        assertEquals("En vérité, j'suis à ma place quand je les dérange. Jamais dans la tendance, toujours dans la bonne direction... #Lille #Montréal #Madrid \uD83D\uDC8D\uD83C\uDDE9\uD83C\uDDFF\uD83C\uDDEE\uD83C\uDDF9", userV2.getDescription());
    }

    @Test
    public void testGetUserFollowersCount() {
        assertEquals(5946, userV2.getFollowersCount());
    }

    @Test
    public void testGetUserFollowingCount() {
        assertEquals(2578, userV2.getFollowingCount());
    }

    @Test
    public void testGetUserTweetsCount() {
        assertEquals(38175, userV2.getTweetCount());
    }


    @Test
    public void testGetUserDateOfCreation() {
        assertEquals(JsonHelper.getDateFromTwitterDateV2("2009-11-23T17:53:15.000Z"), userV2.getDateOfCreation());
    }

    @Test
    public void testGetUserLastUpdate(){
        assertEquals(JsonHelper.getDateFromTwitterDateV2("2020-01-29T07:21:31.000Z"), userV2.getLastUpdate());
    }
}


