# Silicon
Silicon game files:
+ SiliconGame.java (Top-level Class);
+ various other .java files (other Classes);
+ Tune.java (mp3 player support);
+ Tone.java, Envelope.java and Twain.java (threaded sound player support); and
+ resources.zip (3 sub-folders with resources: /data, /images and /sounds).

These files are the result of the extensive recent pruning and harmonisation of the team's codebase.  You can now play through an entire game on the generic background, and I am working, with my coding colleagues of course, to improve the user experience ...

To compile:
1. Put all the .java files in the /src folder.
2. Unzip the resources.zip file into the /src folder (it should create 3 sub-folders: /src/data, /src/images and /src/sounds.
3. Create a /bin folder alongside the /src folder (i.e. both should be sub-folders of your Project folder, as in Silicon/src and Silicon/bin).
4. Either: (a) re-unzip the resources.zip file into the /bin folder (it should create 3 more sub-folders: /bin/data, /bin/images and /bin/sounds; or (b) just copy those three sub-folders from the /src folder to the /bin folder.
5. Create the Silicon Project using SiliconGame as the main class, /src as the source folder, and /bin as the output folder.
