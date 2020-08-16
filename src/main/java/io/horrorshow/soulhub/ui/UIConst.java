package io.horrorshow.soulhub.ui;

import org.springframework.data.domain.Sort;

public class UIConst {

    public static final String TITLE = "SOULHub";
    public static final String DESCRIPTION = "SOULPatches Exchange";

    public static final String ROUTE_EMPTY = "";
    public static final String ROUTE_ABOUT = "about";
    public static final String ROUTE_ADMIN = "admin";
    public static final String ROUTE_EDIT_SOULPATCH = "edit";
    public static final String ROUTE_EDIT_SPFILE = "spfile";
    public static final String ROUTE_LOGIN = "login";
    public static final String ROUTE_LOGOUT = "logout";
    public static final String ROUTE_REGISTER = "register";
    public static final String ROUTE_SOULPATCHES = "soulpatches";
    public static final String ROUTE_USERINFO = "user";
    public static final String ROUTE_CONFIRM_REGISTER = "confirm";
    public static final String ROUTE_SOULPATCH = "soulpatch";
    public static final String ROUTE_PLAYGROUND = "playground";

    public static final String TITLE_ABOUT = TITLE + " | About";
    public static final String TITLE_ADMIN = TITLE + " | Admin";
    public static final String TITLE_EDIT_SOULPATCH = TITLE + " | Edit SOULPatch";
    public static final String TITLE_EDIT_SPFILE = TITLE + " | Edit SOULPatch file";
    public static final String TITLE_LOGIN = TITLE + " | Login";
    public static final String TITLE_REGISTER = TITLE + " | Register";
    public static final String TITLE_SOULPATCHES = TITLE + " | SOULPatches";
    public static final String TITLE_USERINFO = TITLE + " | Userinfo";
    public static final String TITLE_CONFIRM_REGISTER = TITLE + " | Confirm Registration";
    public static final String TITLE_SOULPATCH = TITLE + " | SOULPatch";
    public static final String TITLE_PLAYGROUND = TITLE + " | Playground";

    public static final String LINK_TEXT_LOGOUT = "Logout";
    public static final String LINK_TEXT_SOULPATCHES = "SOULPatches";
    public static final String LINK_TEXT_ABOUT = "About";
    public static final String LINK_TEXT_ADMIN = "Administration";
    public static final String LINK_TEXT_REGISTER = "Registration";
    public static final String LINK_TEXT_LOGIN = "Login";
    public static final String LINK_TEXT_CONFIRM_REGISTER = "confirm registration";

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String PARAM_SHOW_BY_CURRENT_USER = "curuser";
    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_SPFILE = "file";

    public static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;
    public static final String[] ORDER_SORT_FIELDS_SOULPATCHES = {"name", "noViews"};
}
