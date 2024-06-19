
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
Install intelliJ IDEA and perform the following steps

# Main - EDIT CONFIGURATION:

<img width="271" alt="Screenshot 2024-06-19 alle 12 01 16" src="https://github.com/d4rklinux/ObjectOrientation-Wikipedia/assets/78174817/bcdc1d53-14b3-46ec-bb6f-8996a01f3f63">

Select "+" - Application

insert sample.Main into build and run

<img width="532" alt="Screenshot 2024-06-19 alle 14 47 54" src="https://github.com/d4rklinux/ObjectOrientation-Wikipedia/assets/78174817/e21fcccd-93a9-4996-879f-429c91e9c9c4">

# Modify options  

Add VM Options

Enter the folder Path
Insert 
    
    --module-path Users/YourUsername/YourFolder/Wikipedia/Modules/javafx-sdk-21.0.1/lib --add-modules=javafx.controls,javafx.fxml 

Working directory:
Enter the path to the wikipedia folder

Folder Wikipedia
    
    /Users/YourUsername/YourFolder/ObjectOrientation-Wikipedia-main/Wikipedia
    
<img width="797" alt="Screenshot 2024-06-19 alle 13 38 23" src="https://github.com/d4rklinux/ObjectOrientation-Wikipedia/assets/78174817/a1aec8fe-02cf-4e47-be38-b62b2f3b1f0e">


# File

    Project Structure
    Project: SDK: azul-16

Modules
    
    Cartella progetto Dependencies: openjdk-21 (java version “21.0.2”)
    lib
    postgresql-42-7.1.jar

Platform Settings 
# Modules
    
    Dependencies project folder: openjdk-21 (java version “21.0.2”)
    lib
    postgresql-42-7.1.jar
    
<img width="1257" alt="Screenshot 2024-06-19 alle 13 48 20" src="https://github.com/d4rklinux/ObjectOrientation-Wikipedia/assets/78174817/185ed61a-6318-4c34-aac0-92eb08696591">

# Library

Make sure the libraries (lib) and PostgreSQL are properly installed

<img width="1254" alt="Screenshot 2024-06-19 alle 13 48 10" src="https://github.com/d4rklinux/ObjectOrientation-Wikipedia/assets/78174817/f9a50011-c298-4378-a846-4ef776f50cd2">


# Platform Settings 

    SDKs: 
    20
    azul-16
    openjdk-21
    Global Librariers: jafafx-swt

# Problems

If any errors appear, go to "Problems" to resolve them.

Once you have completed all the steps, remember to insert the database into PostgreSQL. In the program, inside the "database" folder under "configs", enter your PostgreSQL password.
