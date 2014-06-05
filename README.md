dadhoo
======
This application is a complete android application to manage photo albums.

The application is divided in two main part Albums and Events. Each album is a container of zero or more events and each event can below to zero or more album.
An event is a generic term to identify a media file: picture, video, vocal record. At the moment only picture are supported.
An album is identified by a title and a picture and represents a container of events.
An event represent a picture with a comment and a date.

When created an event can be linked to one or more albums. 

The application use SQLite to store locally data. No picture are stored into the database. The database contains only the reference to the physical picture file. Picture are stored into the phone gallery.

Ho it works
=======
Clone the project and run the application. This is a good way to start with android. I hope can help.

License
=======
Copyright 2014

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
