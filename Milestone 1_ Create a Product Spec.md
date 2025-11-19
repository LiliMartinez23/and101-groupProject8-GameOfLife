# **Conway's Game of Life**

## Table of Contents

1. [App Overview](#App-Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
1. [Build Notes](#Build-Notes)

## App Overview

### Description 

**Mobile version of Conway's Game of Life. A cellular automaton created by British mathematician John Horton Conway in 1970. Experience the simulation of evolution determined by it's initial state.**

### App Evaluation

- **Category:** Education and Entertainment
- **Mobile:** Tap as many cells as you want, click start, and observe the evolution of your creation.
- **Story:** Explore the behavior and patterns in a famous mathematical simulation.
- **Market:** Math and computer science enthusiasts.
- **Habit:** For entertainment purposes and to encourage curiosity.
- **Scope:** Basic rule set, and features like start, stop, reset. Possible sharing feature.

## Product Spec

### 1. User Features (Required and Optional)

Required Features:

- **Start button**
- **Stop button**
- **Reset/Clear button**
- **Instructions on how to play the game**
- **Grid**

Stretch Features:

- **Sharing/Export pattern (convert to image)**

### 2. Chosen API(s)

- **Android Graphic API**
    - Used to render Conway's Game of Life grid into a bitmap and save it as a PNG/JPEG.
- **GIF Encoder Library**
    - Used to export an animated GIF of Conway's Game of Life evolution.

### 3. User Interaction

Required Feature

- **Grid**
  - => **User clicks/taps the gray (dead) block (cell).**
  - => **Gray block turns black, indicating it's alive.**
  - => **User repeats until statisfied with pattern.**

- **Start**
  - => **After completing their pattern, user clicks the "Start" button.**
  - => **User observes the evolution of their created pattern.**

## Wireframes

### [BONUS] Digital Wireframes & Mockups
<img width="2675" height="1234" alt="Group 1" src="https://github.com/user-attachments/assets/45966315-09ab-427a-9a96-019ab1dbc51d" />

### [BONUS] Interactive Prototype
![GIF](https://github.com/LiliMartinez23/and101-groupProject8-GameOfLife/blob/main/game-of-life-prototype.gif)

## Build Notes

Here's a place for any other notes on the app, it's creation 
process, or what you learned this unit!  

For Milestone 2, include **2+ Videos/GIFs** of the build process here!

## License

Copyright **2025** **Liliana Martinez and Trevor Dove**

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
