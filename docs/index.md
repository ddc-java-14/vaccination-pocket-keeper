---
title: Overview
description: What is Vaccination PocketKeeper?
menu: Overview
order: 0
---

## Summary

Oftentimes, individuals would like to keep track of which vaccinations they have received.  These records are often held at doctor's offices.  However, some people who were born before 2000 have only paper copies of their vaccination history.  Also, individuals who have moved around a lot may have their vaccination histories spread across multiple doctors' offices.  Thus it can be difficult to keep track of which vaccinations one has had and which vaccinations are needed in the near future.  With the addition of COVID related illnesses, keeping track of which shots were given when is critical, and some people even require proof of vaccination for their jobs or for travel on public transportation.  This app is a personal digital record keeper that keeps track of what vaccinations an individual has had throughout their lifetime as well as what vaccinations are upcoming. 

The application will keep track yearly and lifetime vaccination dates and shots.  That is, users will have the ability to see a list of vaccinations that they have received in the past as well as a list of upcoming vaccinations that they need in the near future.  In order to accomplish these functionalities, users will be able to add new vaccinations to their vaccination history, update vaccination information, delete a vaccination from their history, and see a list of CDC and additional  vaccination recommendations (from a phone browser).  

## Intended users

* Someone who wants to have a digital copy of their vaccination history.

    > As someone who wants a digital copy of my vaccination history, I want to record and keep important vaccinations and received dates, so that I can reference them when needed and share with doctors, schools, and public transportation systems.

* Someone who wants to know what vaccinations are needed in the next 6 months.

  > As someone who wants to know which vaccinations I need in the next 6 months, I want an application that automatically tells me which vaccinations are due in the next 6 months, so that I can stay up to date with vaccinations and so that I do not have to put my life in the hands of some doctor, who may not have my full vaccination history, telling me that I need a vaccination now.

* Someone who needs to keep track of when their last COVID-19 or flu shots were.  

    > As someone who needs to keep track of when my last COVID-19 vaccination or flu shots were, I want to store my vaccination history and received dates on my phone, so that I can share them with doctors, schools, and public transportation system, as appropriate.

## Functionality

* Create vaccination entry
* Create and view a list of past vaccinations
* Create and view a list of vaccinations needed in the next 6 months
* Update a vaccination entry
* Delete a vaccination entry
* List a few additional resources where individuals can go to get more information about vaccines.
* Take a photo of a vaccination card (such as a COVID-19 vaccination card)
* Store a photo of a vaccination card (such as a COVID-19 vaccination card)

## Persistent data

* Vaccinations and all metadata about vaccinations (e.g.)
  * vaccination name
  * description of vaccination
  * recommended age range to get vaccination
  * name of doctor who administered the last vaccination of its type
  * date vaccination was last administered
  * upcoming vaccination dates that are needed over the lifetime of the individual
  * photo of proof of vaccination for each dose

## Device/external services

This app will use the following services: 
* Service: Google sign-in
  * [Developer Documentation for Google Sign-in](https://developers.google.com/identity/sign-in/android/start-integrating)
  * This app will use this service to authenticate users through a google account.
  * This app will not be able to function without full-time access to Google sign-in services.

* Service: Camera Integration
  * [Developer Documentation for Camera Service](https://developer.android.com/guide/topics/media/camera)
    * [Storing and Retrieving Images in SQLite](https://www.youtube.com/watch?v=X7T0g5kBYJk)
  * [Sample Camera App (in Java)](https://github.com/googlearchive/android-Camera2Basic/blob/master/Application/src/main/java/com/example/android/camera2basic/Camera2BasicFragment.java)
  * This app will use the camera to take a picture of a vaccination card (such as a CDC COVID-19 card) and store the picture in the app.
  * This app will be able to function without full-time access to the camera.
  
* Service: WebView Integration
  * [Developer Documentation for WebView](https://developer.android.com/guide/webapps)
  * This app will use the WebView to render additional resources as specified in [Additional Resources](#additional-resources)
  * This app will be able to function without full-time access to a WebView.

## Additional Resources

These additional resources will be integrated into the WebView: 
* [Recommended Immunization Schedule For Children](https://healthychildren.org/English/safety-prevention/immunizations/Pages/Recommended-Immunization-Schedules.aspx?gclid=CjwKCAjwh5qLBhALEiwAioodsz1-zBQxR35agsYeBt4t1fmXJvEmUJUKcMPg3VOTd_XLX_Uq_h5uXxoCDbsQAvD_BwE)
* [Recommended Immunizations When Traveling Abroad](https://wwwnc.cdc.gov/travel/destinations/traveler/none/american-samoa?s_cid=ncezid-dgmq-travel-single-001)
* [Recommended Immunization Schedule for Adults](https://www.cdc.gov/vaccines/schedules/hcp/imz/adult.html)

**Note**
These resources are subject to change as additional information is discovered.

## Stretch goals/possible enhancements 

* The app will send friendly reminders to the user through email, notification, or SMS when vaccinations are due (for example, tetanus and COVID-19 shots).
* The app will use OCR recognition to automatically fill in vaccination information. 
  * [An OCR Recognition Package](https://github.com/tesseract-ocr/tesseract)
