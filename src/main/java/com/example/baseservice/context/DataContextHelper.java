package com.example.baseservice.context;

import com.example.baseservice.common.DecodedToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

public class DataContextHelper {

    public static void setDataContext(DataContext dataContext) {
        DataContextHolder.setDataContext(dataContext);
    }

    public static DataContext getDataContext() {
        return DataContextHolder.getDataContext();
    }

    public static void setAuthentication(Authentication authentication) {
        //TODO: Thêm xử lý lấy accessToken từ authentication
        User user = (User) authentication.getPrincipal();
//        String accessToken = (String) authentication.getDetails();
//        DecodedToken decodeToken = DecodedToken.parseProperties(accessToken);
        DataContext dataContext = DataContext.builder()
                .username(user.getUsername())
//                .authorities(decodeToken.getAuthorities())
//                .userId(decodeToken.getUuidAccount())
                .build();
        DataContextHolder.setDataContext(dataContext);
    }

    public static void clear() {
        DataContextHolder.clear();
    }

    public static String getUsername() {
        return getDataContext().getUsername();
    }

    public static String getUserId() {
        return getDataContext().getUserId();
    }

    public static Object getOtherData(String key) {
        return getDataContext().getOtherData().get(key);
    }

    public static Object getOtherData(Enum<?> key) {
        return getDataContext().getOtherData().get(key);
    }

    public static void setOtherData(Enum<?> key, Object value) {
        getDataContext().getOtherData().put(key, value);
    }

    public static void removeOtherData(Enum<?> key) {
        getDataContext().getOtherData().remove(key);
    }

    public static void removeAuthority(String key) {
        getDataContext().getAuthorities().remove(key);
    }

    public static void removeAuthority(Enum<?> key) {
        getDataContext().getAuthorities().remove(key.name());
    }


}
