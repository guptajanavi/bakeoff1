# BakeOff 1
This code was written as part of the Spring 2024 version of 05-391 (Designing Human-Centered Software) at Carnegie Mellon University. The goal of the project was to create a more efficient and intuitive alternative to the standard digital whack-a-mole game interface. 

## Overview
- Each square represents a potential position where the mole could be
- This interface is to be interacted with a trackpad and the spacebar. The trackpad is used to navigate the hammer to where the mole is and the spacebar is used to whack the mole
- There are 16 different moles to whack and your final score depends upon the combination of the time you took and how accurately you whacked the moles

## Initial
The first screen just consists of grey, cyan and red squares. The grey squares represent the non-target square, the blue square represents the current target and the red square represents the position of the next target.

![First Screen](https://github.com/guptajanavi/whack_a_mole/assets/79553848/dd7a0728-90a0-48da-82c6-6b08ba797a12)

## Hovering
Hovering over the target square turns it green. This means that clicking on the target when it is green will register it as a "hit".

![Hovering](https://github.com/guptajanavi/whack_a_mole/assets/79553848/07ddf953-1248-44c7-a07f-b76a4c52a820)

## Path to Next Target
Once you hit the first target, for every subsequent target a path to the next target appears. This makes it easier for the users to tell which direction to move their cursor in and allows them to simply follow that path. 

![Path](https://github.com/guptajanavi/whack_a_mole/assets/79553848/b2c469cc-ce41-400a-8a87-5468d36a8303)

## Statistics
Once you are done with all the 16 hits, the game ends and displays several useful statistics

![Statistics](https://github.com/guptajanavi/whack_a_mole/assets/79553848/e4ba9902-35cd-4a22-adf2-054a7a73ae29)
