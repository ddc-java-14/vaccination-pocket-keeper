---
title: Overview
description: What is Vaccination PocketKeeper?
menu: Overview
order: 0
---

## Summary

Oftentimes, individuals would like to keep track of which vaccinations they have received. These records are often held at doctor’s offices. However, some people who were born before 2000 have only paper copies of their vaccination history. Also, individuals who have moved around a lot may have their vaccination histories spread across multiple doctors’ offices. Thus, it can be difficult to keep track of which vaccinations one has had and which vaccinations are needed in the near future. With the addition of COVID-19 related illnesses, keeping track of which shots were given when is critical, and some people even require proof of vaccination for their jobs or for travel on public transportation. This app is a personal digital record keeper that keeps track of what vaccinations an individual has had throughout their lifetime as well as what vaccinations are upcoming.

The application tracks yearly and lifetime vaccination dates and shots in digital, easy to use format. Through this app, users have the ability to see a list of vaccinations that they have received in the past as well as a list of upcoming vaccinations that they need in the near future. In order to accomplish these functionalities, users can add new vaccinations to their vaccination history, update vaccination information, and delete a vaccination from their history.

This app includes several features which enable vaccination tracking and history.  First, users can enter a set of doctors who administer a shot.  A doctor consists of a doctor name.  Users can also edit and delete a doctor entry.  Second, users can enter a vaccination.  A vaccination consists of a name, a frequency of doses, and a total number of doses to give for the vaccine, and finally, an age range of the vaccine (e.g. 0-5 years old, 5-15 years old, 25-65 years old, 65-85 years old).  Users can edit and delete a vaccine and all of its information.  Third, users can enter a number of doses for each vaccine.  A dose represents an administration of the vaccine on a particular date by a particular doctor.  Doses can also be edited and deleted.  Fourth, users can see a list of upcoming vaccines.  These are vaccines that are due in the future.  Users can specify, through a setting option, how many years in the future to view upcoming vaccinations.  Finally, users can take a picture of a vaccination card, such as a COVID-19 card, from the phone camera.


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
* Delete a vaccination entry <!-- (* List a few additional resources where individuals can go to get more information about vaccines.)-->
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
  * This app uses this service to authenticate users through a Google account.
  * This app will not function without full-time access to Google sign-in services.

* Service: Camera Integration
  * This app uses the camera to take a picture of a vaccination card (such as a CDC COVID-19 card) and store the picture in the app.
  * This app will function without full-time access to the camera.

### Additional Resources

These additional resources will be integrated into the WebView:
* [Recommended Immunization Schedule For Children](https://healthychildren.org/English/safety-prevention/immunizations/Pages/Recommended-Immunization-Schedules.aspx?gclid=CjwKCAjwh5qLBhALEiwAioodsz1-zBQxR35agsYeBt4t1fmXJvEmUJUKcMPg3VOTd_XLX_Uq_h5uXxoCDbsQAvD_BwE)
* [Recommended Immunizations When Traveling Abroad](https://wwwnc.cdc.gov/travel/destinations/traveler/none/american-samoa?s_cid=ncezid-dgmq-travel-single-001)
* [Recommended Immunization Schedule for Adults](https://www.cdc.gov/vaccines/schedules/hcp/imz/adult.html)

**Note**
These resources are subject to change as additional information is discovered.

## Summary of current state of the app

#### Description of the current state of completion/readiness of the app

This app is a solid working prototype. The core functionality works, but there are several deficiencies.  

* When a user adds a dose to a vaccine, it does not update the number of doses in the vaccine entity in the database.  
* When a user takes a photo, it does not store the photo as a URI and put it in the database for each dose as appropriate.
* Photos cannot be retrieved from the database; rather they must be retrieved by the user going to the photos app on the phone.  

List of aesthetic/cosmetic (not functional) enhancements

* A color coordinated schema would enhance this app.  
* Changing the green background of the drawer as well as the words and email address would enhance this app.

## Stretch goals/possible enhancements

* There should be a listing of all past vaccinations within a certain date range.  
* It would be nice to have the next upcoming doses calculated by taking the user birthday in conjunction with age range to determine these.  
* The app will send friendly reminders to the user through email, notification, or SMS when vaccinations are due (for example, tetanus and COVID-19 shots).
* The app will use OCR recognition to automatically fill in vaccination information.
  * [An OCR Recognition Package](https://github.com/tesseract-ocr/tesseract)
* The app will integrate a WebView Service
  * [Developer Documentation for WebView](https://developer.android.com/guide/webapps)
  * This app will use the WebView to render additional resources as specified in [Additional Resources](#additional-resources)
  * This app will be able to function without full-time access to a WebView.

## Wireframe diagram

[Wireframe](wireframe.md)

## ERD

[ERD](erd.md)

## DDL

[DDL](ddl.md)

## Technical requirements & dependencies

### Android API versions and hardware (including emulators) on which you’ve tested

* Hardware versions on which this app has been tested.
  * Pixel 3a API 28
* Minimum Android AP required
  *26

### List of the 3rd-party libraries

* Google Sign In
* Gson
* ReactiveX 
* CameraX

### List of Android permissions required to run the app

* Permissions Required to Run the App
  * Safe permissions
    * None
  * Dangerous permissions
    * Camera
      * Write and read to external storage
         * If the user does not allow both of these permissions, then the user cannot store a photo to file storage on the phone.  

### A list of the external services consumed by the app

* Google Sign In


## Javadocs

[Javadocs](javadocs.md)

## Copyrights & licenses

[Copyrights & Licenses](notice.md)

## Build instructions

#### Clone/download the repository

The repository can be cloned from the following link.

[Github link to repository](https://github.com/ddc-java-14/vaccination-pocket-keeper/)

Import the project into Android Studio/IntelliJ IDEA.

A standard clone from VCS will import the project into IntelliJ

Execute the build.

There are no special build instructions.  

## Basic user instructions

Sign in to the app using a google sign in account.
The app comes pre-loaded with several doctors, vaccines, and doses. 
Add new/edit/delete vaccines, doctors, and doses.
When on the vaccines screen, clicking on a vaccine will take the user to a detailed dose for each vaccine screen.
Take a photo of a vaccination card (e.g. COVID-19 proof of vaccination).  
- Select a Dose to associate the picture with. 
- This is stored in the local database under the applicable Dose.

## Implementation

[Implementation](implementation.md)

## Copyright Notice

Copyright 2021 Cynthia Nikolai. All rights reserved.