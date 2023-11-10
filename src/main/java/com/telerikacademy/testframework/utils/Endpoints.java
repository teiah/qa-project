package com.telerikacademy.testframework.utils;

public class Endpoints {

    // Authenticate
    public static String AUTHENTICATE = "/authenticate";

    // Admin-Controller
    public static final String ADMIN = "/admin";
    public static final String ADMIN_STATUS = "/admin/status";
    public static final String ADMIN_SHOW_USERS = "/admin/users";

    // Login-Controller
    public static final String LOGIN = "/login";
    public static final String LOGIN_ACCESS_DENIED = "/login/access-denied";

    // Registration-Controller
    public static final String REGISTER = "/register";

    // Rest-Post-Controller
    public static final String POST = "/post/";
    public static final String CREATE_POST = "/post/auth/creator";
    public static final String EDIT_POST = "/post/auth/editor";
    public static final String LIKE_POST = "/post/auth/likesUp";
    public static final String DELETE_POST = "/post/auth/manager";
    public static final String COMMENTS_OF_POST = "/post/Comments";


    // Post-Controller
    public static final String POST_FEED = "/posts";
    public static final String POST_SHOW = "/posts/%s";
    public static final String POST_SHOW_WITH_ID = "/posts/%s";
    public static final String POST_IMAGE = "/posts/%s/postImage";
    public static final String POST_EDIT_WITH_ID = "/posts/auth/editor/%s";
    public static final String POST_DELETE_WITH_ID = "/posts/auth/manager/%s";
    public static final String POST_NEW = "/posts/auth/newPost";

    // Rest-Comment-Controller
    public static final String COMMENT_ALL = "/comment";
    public static final String CREATE_COMMENT = "/comment/auth/creator";
    public static final String EDIT_COMMENT = "/comment/auth/editor";
    public static final String LIKE_COMMENT = "/comment/auth/likesUp";
    public static final String DELETE_COMMENT = "/comment/auth/manager";
    public static final String COMMENT_BY_POST = "/comment/byPost";
    public static final String COMMENT_SINGLE = "/comment/single";

    // Comment-Controller
    public static final String COMMENT_EDIT_WITH_ID = "/comments/editor/%s";
    public static final String COMMENT_DELETE_WITH_ID = "/comments/manager/%s";

    // Rest-Connection-Controller
    public static final String REQUEST = "/auth/request";
    public static final String USER_REQUEST_WITH_ID = "/auth/users/%s/request/";
    public static final String APPROVE_REQUEST_WITH_ID = "/auth/users/%s/request/approve";

    // Connection-Controller
    public static final String USER_REQUESTS_WITH_ID = "/auth/connection/%s/request";
    public static final String REQUEST_CONNECTION = "/auth/connection/request";
    public static final String REQUEST_CONNECTION_APPROVE = "/auth/connection/request/approve";

    // Picture-Controller
    public static final String USER_IMAGE_WITH_ID = "/users/%s/userImage";

    // Home-Controller
    public static final String ABOUT_US = "/about-us";
    public static final String AUTH = "/auth";

    // Profile-Controller
    public static final String USER_PROFILE_WITH_ID = "/auth/users/%s/profile";
    public static final String EDIT_USER_FORM_PROFILE_WITH_ID = "/auth/users/%s/profile/editor";
    public static final String EDIT_USER_EXPERTISE_PROFILE_WITH_ID = "/auth/users/%s/profile/expertise";
    public static final String EDIT_USER_PROFILE_WITH_ID = "/auth/users/%s/profile/personal";
    public static final String PROFILE_POSTS_WITH_ID = "/auth/users/%s/profile/posts";
    public static final String EDIT_PROFILE_PICTURE_WITH_ID = "/auth/users/%s/profile/settings";

    // Rest_User_Controller
    public static final String USERS = "/users";
    public static final String REGISTER_USER = "/users/";
    public static final String USER_POSTS_WITH_ID = "/users/%d/posts";
    public static final String USER_BY_ID = "/users/auth/%d";
    public static final String UPGRADE_USER_EXPERTISE_WITH_ID = "/users/auth/%s/expertise";
    public static final String UPGRADE_PERSONAL_PROFILE = "/users/auth/%s/personal";

    // Search-Controller
    public static final String SEARCH_USERS = "/search";

    // Rest_Skill_Controller
    public static final String FIND_SKILL = "/skill";
    public static final String CREATE_SKILL = "/skill/create";
    public static final String DELETE_SKILL = "/skill/delete";
    public static final String EDIT_SKILL = "/skill/edit";
    public static final String GET_ONE_SKILL = "/skill/getOne";

    // Basic-Error-Controller
    public static final String ERROR = "/error";
}
