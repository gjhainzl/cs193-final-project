# Project Name

One-sentence description of what it does.

## The Problem
Trying to find dates and times that a large group of people will be available on.

## How It Works

The program allows you to build up multiple peoples calendars and find'
what days the most people are available on.

//Note, this diagram was made before I learned about JSwing and started 
making the project using that as a basis.

![System diagram](docs/FlowChart.drawio.png)

## Tech Stack

- **Language:** Java
- **Libraries:** javax.swing.*;
   java.awt.*;
   java.io.IOException;
   java.io.ObjectInputStream;
   java.io.ObjectOutputStream;
   java.nio.file.Files;
   java.nio.file.Path;
   java.nio.file.Paths;
   java.time.LocalDate;
   java.util.ArrayList;
   java.util.Collections;
   java.util.List;
- **External tools:** IntelliJ

## Setup
git clone https://github.com/gjhainzl/cs193-final-project
cd cs193-final-project/src/main
java run Main

## Usage

After running Main JSwing window will appear with all the functions
of the program. 
Using this program involves creating different peoples calendars, each
calendar can be given the days that person is or isnt available on.
The program then has a button to show a ranking of the the best dates that line up
for all the days in the calendars.

## Roadmap

- [Completed] Non Terminal Interface
- [ ] Allowing import of pre-made calendars that can be added to a schedule.
- [ ] Make it easier to add days to a calendar

## Author

Gabe Hainzl, github.com/gjhainzl