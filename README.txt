#This readme is to comilation of help for lhor612 and sbut576
#and is a general help guide

--------------------------------------------------------------------
Contents
--------------------------------------------------------------------
1.Startup
2.Opening files
3.Downloading files
4.Editing the audio
5.Editing in text
--------------------------------------------------------------------

--------------------------------------------------------------------
1.STARTUP
--------------------------------------------------------------------

Upon getting VAMIX, you must run it using the console. Navigate to the directory of the VAMIX using cd [directory] and cd .. to get around. Once in the directory, use the command below to allow running of the .jar file.

>java -jar VAMIX.jar *****************************

Once running, it may take a short time to begin on slower machines.

--------------------------------------------------------------------
2.Opening files
--------------------------------------------------------------------

Once the VAMIX is running, you can select files from directories on your system utilising the OPEN button, and then navigating through directories using the panel. Once a file is selected, there are 2 options. ACCEPTING or DECLINING:

ACCEPTING:
This is where the file has been accepted, meaning that it is a video (simple only) type file, which is always accepted by VAMIX. This may or may not allow video editing depending on audio channels *(see Editing the audio section for help)*.

DECLINING:
This is where the file is not accepted. A pop-up message will appear to tell you that the file selected is not allowed. Though it may be an video/audio file, the VAMIX setup only allows more simple styles of those types, excluding things such as ".rmvb" files.

Once selection has occured, you can continue with other processes in the VAMIX app. This will also start the playing of the video.

--------------------------------------------------------------------
3.Downloading Files
--------------------------------------------------------------------

Downloading of file is done utilising the "wget" command from the LINUX family of commands. Thus, this downloading option will only occur on machines using linux or some other process that allows this command. This command can only be reached by accepting that the downloadable file is one that is OPEN-SOURCE. VAMIX holds no responsibility for copyright infringement due to usage of this to download from non-open-source content hosting websites.

Once the command is begun, you may cancel the operation using the "CANCEL" button. This button will stop the process, but the file up to the point of canceling will still exist on your machine. If you do not want this file anymore, you will need to delete it by navigating to it using your own file browsing software. ***********************The command automatically continues downloads if you hit cancel and did not want it, by replacing the current one and redownloading it from scratch to stop any odd problems from occuring due to small sections of file not being downloaded correctly.

When the command completes, there are 2 outcomes, SUCCESS or FAILURE:

SUCCESS:
This is where the file has been downloaded. Once this has occured, you need to navigate to the file downloaded using the opening panel *(see above for the open panel information)* to start the file in the VAMIX app. From there, you can continue with whatever operations (VAMIX BASED) you wish.

FAILURE:
If the VAMIX system fails, then either the URL that was entered is not correct, or your connection is not currently running/is having problems. Try again at a later time upon checking connection and check the URL as well to make sure that the problem does not lie there. If the problem persists, then please contact logan.horton@live.com for a bug report.

--------------------------------------------------------------------
Editing the audio
--------------------------------------------------------------------

To edit audio, you use the panel with audio selection on it. It also contains the outname for both the text addition panel *(see below for information on the text editing panel)*. To begin using this panel, enter the audio you wish to use to manipulate, or utilise the strip command on the video(with audio) that you entered.
There is a strip, replace and a overlay command, listed below:

STRIP:
The strip command does not require an audio file input in the audio panel. This command will only work on video files with an audio channel. This command will allow you to mute the audio, and if you click "Yes" on the pop-up box that occurs, will also output an audio file with similar appropriate name to your VAMIX.jar's directory, named using the outname box which must be filled in to allow button clicking enabling. 

REPLACE:
The replace command requires an audio input to be put into the panel, and will replace the audio on the file selected, saving the new audio and video to a file named using the outname box's input, which must be enabled to allow button clicking.

OVERLAY:
The overlay command allows you to overlay the current audio on the cideo with another audio. This requires the audio box to be entered using the file selector, and the video input in the input panel *(see above for information on the input panel)* which requires audio to be on it to occur. 

Issues that may occur:
If you enter a file that is not correct, you will get an error message telling you that you must select an audio file. The buttons each can reject input if the files are not correct, and are not enabled unless a video file has been added through input panel *(see above for input panel information)* which will allow the buttons to be used. The buttons also will let you know if the outname you have selected is one existing, asking you if you wish to overwrite the file or not, selecting no being to cancel the operation, yes being to overwrite and make the command operate. 

The other option comes from the strip command where you may select an option between creating an audio file as well or not. This will not remove the audio from the origin file, but will create 2 new files (1 the muted video, one the audio of that video) which are placed in the same directory as that of the VAMIX.jar. 

--------------------------------------------------------------------
Editing the text
--------------------------------------------------------------------








