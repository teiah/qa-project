package api.utils;

public class JSONRequests {

    public static final String userBody = "{\n" +
            "  \"authorities\": [\n" +
            "    %s\n" +
            "  ],\n" +
            "  \"category\": {\n" +
            "    \"id\": %s,\n" +
            "    \"name\": \"%s\"\n" +
            "  },\n" +
            "  \"confirmPassword\": \"" + "%s" + "\",\n" +
            "  \"email\": \"" + "%s" + "\",\n" +
            "  \"password\": \"" + "%s" + "\",\n" +
            "  \"username\": \"" + "%s" + "\"\n" +
            "}";
    public static final String personalProfileBody = "{\n" +
            "\"birthYear\": \"%s\",\n" +
            "\"firstName\": \"%s\",\n" +
            "\"id\": %d,\n" +
            "\"lastName\": \"%s\",\n" +
            "\"location\": {\n" +
            "\"city\": {\n" +
            "\"city\": \"%s\",\n" +
            "\"country\": {},\n" +
            "\"id\": 1\n" +
            "},\n" +
            "\"id\": 0\n" +
            "},\n" +
            "\"memberSince\": \"\",\n" +
            "\"personalReview\": \"%s\",\n" +
            "\"picture\": \"%s\",\n" +
            "\"picturePrivacy\": %s,\n" +
            "\"sex\": \"%s\"\n" +
            "}";
    public static final String personalProfileBodyFirstName = "{\n" +
            "\"firstName\": \"%s\",\n" +
            "\"location\": {\n" +
            "}\n" +
            "}";
    public static final String expertiseProfileBpdy = "{\n" +
            "  \"availability\": %d,\n" +
            "  \"category\": {\n" +
            "    \"id\": %d,\n" +
            "    \"name\": \"%s\"\n" +
            "  },\n" +
            "  \"id\": 0,\n" +
            "  \"skill1\": \"%s\",\n" +
            "  \"skill2\": \"%s\",\n" +
            "  \"skill3\": \"%s\",\n" +
            "  \"skill4\": \"%s\",\n" +
            "  \"skill5\": \"%s\"\n" +
            "}";
    public static final String postBody = "{\n" +
            "  \"content\": \"" + "%s" + "\",\n" +
            "  \"picture\": \"" + "%s" + "\",\n" +
            "  \"public\": " + "%s" + "\n" +
            "}";
    public static final String sendRequestBody = "{\n" +
            "  \"id\": %s,\n" +
            "  \"username\": \"%s\"\n" +
            "}";
    public static final String searchUsersBody = "{\n" +
            "  \"index\": %s,\n" +
            "  \"next\": %s,\n" +
            "  \"searchParam1\": \"%s\",\n" +
            "  \"searchParam2\": \"%s\",\n" +
            "  \"size\": %s\n" +
            "}";

    public static final String commentBody = "{\n" +
            "  \"commentId\": 0,\n" +
            "  \"content\": \"%s\",\n" +
            "  \"deletedConfirmed\": %s,\n" +
            "  \"postId\": %s,\n" +
            "  \"userId\": %s\n" +
            "}";
    public static final String skillBody = "{\n" +
            "  \"category\": {\n" +
            "    \"id\": %d,\n" +
            "    \"name\": \"%s\"\n" +
            "  },\n" +
            "  \"skill\": \"%s\",\n" +
            "  \"skillId\": 0\n" +
            "}";
}
