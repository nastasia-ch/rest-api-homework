package models;

import lombok.Data;

import java.util.List;

@Data
public class ListUsersResponseModel {

   private int page,per_page,total,total_pages;
   private List<User> data;
   private Support support;

   @Data
   public static class User{
      private int id;
      private String email,first_name,last_name,avatar;
   }

   @Data
   public static class Support {
      private String url,text;

   }

}
