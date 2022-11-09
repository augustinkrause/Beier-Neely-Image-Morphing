
# Beier-Neely Image Morphing  

This is a fun little project that I programmed in my own time. It implements the [Beier-Neely image morphing algorithm](https://www.cs.princeton.edu/courses/archive/fall00/cs426/papers/beier92.pdf) and provides a GUI with it. I also included a few sample videos and photograpies of mine, that can be used to test the GUI.  

***

## A few Heads-ups

### Start-Up

* The GUI requires two images to be loaded upon start-up. The code currently uses some images on my computer. You probably need to change the paths in the code (see file GUI.java, line 26 and line 32) and rebuild the artifact in order for the GUI to work.
* To compile the videos, the GUI requires a ffmpeg installation as a dependency. You need to install this manually and then specify the path to the installation in the code (file GUI.java, line 299).

### Sample Videos

* You might notice a few glitches in some of the provided sample videos. These are intentional; they stem from not properly "drawing the lines" when morphing two images. I thought they looked interesting. To avoid this, always take care when drawing the to-be-morphed lines in the reference and target image!

## GUI Walkthrough

