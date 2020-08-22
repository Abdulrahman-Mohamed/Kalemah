package com.Motawer.kalemah.Auth;

import android.net.Uri;

public class UserModel {
  Uri image;
  private String username, email;

  public UserModel()
  {
  }

  public UserModel(String username, String email) {
    this.username = username;
    this.email = email;
  }

   /* public UserModel(Uri image, String username, String email)
    {
        this.image = image;
        this.username = username;
        this.email = email;
    }*/

  public Uri getImage() {
    return image;
  }

  public void setImage(Uri image) {
    this.image = image;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
