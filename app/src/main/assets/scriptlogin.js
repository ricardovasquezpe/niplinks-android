setTimeout(function(){
  try {
      if (form_field) {
          document.querySelector(form_field).click();
      }document.querySelector(us_field).value = username;
      document.querySelector(pass_field).value = password;
      document.querySelector(btn_field).click();
  } catch(err) {
      Android.someError(err.message);
  }
}, 1000);