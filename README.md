# Silicon
Silicon game files:
+ SiliconGame.java (Top-level Class);
+ various other .java files (other Classes);
+ Tune.java (mp3 player support);
+ Tone.java, Envelope.java and Twain.java (threaded sound player support); and
+ resources.zip (4 sub-folders with resources: /data, /images and /sounds plus /images/cards).

These files are the result of the extensive recent pruning and harmonisation of the team's codebase.  You can now play through an entire game on the "generic + research level 0" background, and I am working, with my coding colleagues of course (i.e. Dao), to improve the user experience ...

To compile, do the following in Windows Explorer:

1. Put all the .java files in the /src folder.  This is usually in "C:\Users\\[username]\eclipse-workspace\\[projectname]" for Windows PCs, not so sure about other OSs.
2. Unzip the resources.zip file into the /src folder (it should create 4 sub-folders: /src/data, /src/images and /src/sounds.  Yes, that's only 3: /src/images has its own sub-folder: /src/images/cards).
3. Is there a /bin folder alongside your /src folder (i.e. both should be sub-folders of your Project folder, as in Silicon/src and Silicon/bin)?  If not, you'll need to create one.
4. Either: (a) re-unzip the resources.zip file into the /bin folder (it should create 4 more sub-folders: /bin/data, /bin/images, /bin/images/cards and /bin/sounds); or (b) just copy the three top sub-folders from the /src folder to the /bin folder.


Then, do the following in Eclipse (or the equivalent in your IDE of choice):
1. If you need to, create the Silicon Project using SiliconGame as the main class, /src as the source folder, and /bin as the output folder (these folders are usually the default so they may not require any special settings).
2. Right-click on the "[projectname]" file in Package Explorer and select "Refresh" from the drop-down menu (or hit F5). This should tell the Package Explorer about the resource folders you've added.
3. Set up your compiler: Right-click on the "[projectname]" file in Package Explorer and select "Properties" from the drop-down menu.  We need to set Java 8 as the runtime environment.
4. Select "Java Compiler" from the menu on the left, click on "Enable project specific settings" on the right, and select "1.8" for the Compiler compliance level.  This is actually Java 8.  If it is not in the drop-down list, you will need to download Java 8 from Oracle's website and set it as the target JRE (once you've downloaded and installed the Java 8 JRE, go to "Java Build Path" in the menu, click on the "Libraries" tab, then on the "Add Library" button and select it from the list).
5. If you make a change to the resources (e.g. by changing the CSS styles in SiliconStyles.css), then you should click on "Project" (or press Alt+P), select "Clean" from the drop-down menu, ensure your [projectname] is ticked, and then click the "Clean" button.  After that, you can run the game as normal with "Run --> Run As --> Java Application" (or [ALT+SHIFT+X], followed by J).
