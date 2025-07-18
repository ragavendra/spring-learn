package com.stopsnearme.app.ws.userservice;

import com.stopsnearme.app.ws.ui.model.request.UserDetailsRequestModel;
import com.stopsnearme.app.ws.ui.model.response.UserRest;

public interface UserService {
	UserRest createUser(UserDetailsRequestModel userDetails);
	UserRest fetchUser(String uId);
}
