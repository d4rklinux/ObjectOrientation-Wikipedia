
# ObjectOrientation-Wikipedia

Object Orientation project for the Federico II University of Naples

Description:
WikiJava is a web application similar to Wikipedia, developed in Java, which allows users to create, edit and consult encyclopedic content. The system implements a collaboration model between authors and users, guaranteeing the quality and reliability of the information.

Main features:

    User Management:
        Authors:
            Create and edit pages
            Search pages
            Manage changes proposed by users (approve, reject)
            Receive positive feedback for approved changes
            
        Users:
            Search and view pages
            Propose changes to the texts (with request for approval from the author)

Used technologies:

    Programming language: Java
    Database: [PostgreSQL]


Implementation:

The project is divided into main modules:

    User management module: authentication, permissions, roles.
    Page management module: creation, modification, search, version control.
    Change proposal module: management of changes proposed by users, feedback to authors.
    Search engine module: efficient search within textual content.

# IntelliJ IDEA Mac APP Opening Procedure

Main - EDIT CONFIGURATION:

<img width="271" alt="Screenshot 2024-06-19 alle 12 01 16" src="https://github.com/d4rklinux/ObjectOrientation-Wikipedia/assets/78174817/2aa28353-b455-4607-aa22-906e8ff567ab">


Enter the folder Path
Build and run Insert
Insert 
    
    --module-path YourFolder/Wikipedia/Modules/javafx-sdk-21.0.1/lib --add-modules=javafx.controls,javafx.fxml 

Working directory:
Enter the path to the wikipedia folder

    Folder Wikipedia

<img width="801" alt="Screenshot 2024-06-19 alle 12 01 38" src="https://github.com/d4rklinux/ObjectOrientation-Wikipedia/assets/78174817/8b37e9f0-ffad-447f-8eff-2ef5e2bcc868">

File

    Project Structure
    Project: SDK: azul-16

Modules
    
    Cartella progetto Dependencies: openjdk-21 (java version “21.0.2”)
    lib
    postgresql-42-7.1.jar

Platform Settings 

    SDKs: 
    20
    azul-16
    openjdk-21
    Global Librariers: jafafx-swt

    

