package player;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

import com.sun.jna.NativeLibrary;

@SuppressWarnings("serial")
public class MediaPlayerPanel extends Panel
{
  protected static final String[] DEFAULT_FACTORY_ARGUMENTS = {
    "--video-title=vlcj video output",
    "--no-snapshot-preview",
    "--quiet",
    "--quiet-synchro",
    "--sub-filter=logo:marq",
    "--intf=dummy"
  };
  
  String mrl;
  
  Canvas canvasMediaPlayer;
  CanvasVideoSurface canvasVideoSurface;
  MediaPlayerFactory mediaPlayerFactory;
  EmbeddedMediaPlayer embeddedMediaPlayer;

  
  private Timer t;
  private JPanel screenPanel,controlsPanel;
  private JSlider sliderTimeline;
  private JButton btnPlay,btnBack,btnForward;
  private JToggleButton tbtnMute;
  private JSlider sliderSpeed;
  private String[] mediaOptions = {""};
  private boolean syncTimeline=false;
  private boolean looping=false;
  private SimpleDateFormat ms;
  private int jumpLength=1000;
  private int  loopLength=6000;

  
  public MediaPlayerPanel() {
    this(null);
  }
  
  public MediaPlayerPanel(JFrame frame) {
    try
    {
      //we don't need any options
      String[] standardMediaOptions = {""}; 

      //System.out.println("libvlc version: " + LibVlc.INSTANCE.libvlc_get_version());
      //setup Media Player
      //TODO we have to deal with unloading things....
      
      NativeLibrary.addSearchPath("libvlc", "C:\\Program Files (x86)\\VideoLAN\\VLC");
      
      //setup GUI
      setSize(400, 300); //later we apply movie size
      createUI();
      setLayout();
      addListeners(); //registering shortcuts is task of the outer plugin class!
      //embed vlc player
      
      mediaPlayerFactory = new MediaPlayerFactory(DEFAULT_FACTORY_ARGUMENTS);

      FullScreenStrategy fullScreenStrategy = null;
      
      if( frame != null )
      {
        fullScreenStrategy = new DefaultFullScreenStrategy(frame);
      }
      
      embeddedMediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer(fullScreenStrategy);
      embeddedMediaPlayer.setStandardMediaOptions(standardMediaOptions);
      
      canvasVideoSurface = mediaPlayerFactory.newVideoSurface(canvasMediaPlayer);
      embeddedMediaPlayer.setVideoSurface(canvasVideoSurface);

      this.setVisible(true);
      canvasMediaPlayer.setVisible(true);
      
      //mp.pause();
      //jump(0);
      //create updater
      ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
      executorService.scheduleAtFixedRate(new Runnable() {
        //We have to do syncing in the main thread
        public void run() {
          SwingUtilities.invokeLater(new Runnable() {
            //here we update
            public void run() {
              //if (isPlaying()) updateTime(); //if the video is seeking we get a mess
            }
          });
        }
      }, 0L, 1000L, TimeUnit.MILLISECONDS);
      //setDefaultCloseOperation(EXIT_ON_CLOSE);
      //addWindowListener(this);

    }
    catch (NoClassDefFoundError e)
    {
      System.err.println("Unable to find JNA Java library!");
    }
    catch (UnsatisfiedLinkError e)
    {
      System.err.println("Unable to find native libvlc library!");
    }

  }
  
  public void addMediaPlayerEventListener(MediaPlayerEventListener eventListener)
  {
    embeddedMediaPlayer.addMediaPlayerEventListener(eventListener);
  }
  
  private void createUI() {
    //setIconImage();
    ms = new SimpleDateFormat("hh:mm:ss");
    canvasMediaPlayer=new Canvas();
    canvasMediaPlayer.setBackground(Color.black);
    sliderTimeline = new JSlider(0,100,0);
    sliderTimeline.setMajorTickSpacing(10);
    sliderTimeline.setMajorTickSpacing(5);
    sliderTimeline.setPaintTicks(true);
    //TODO we need Icons instead
    btnPlay= new JButton("play");
    btnBack= new JButton("<");
    btnForward= new JButton(">");
    tbtnMute= new JToggleButton("mute");
    sliderSpeed = new JSlider(-200,200,0);
    sliderSpeed.setMajorTickSpacing(100);
    sliderSpeed.setPaintTicks(true);          
    sliderSpeed.setOrientation(Adjustable.VERTICAL);
    Hashtable<Integer,JLabel> labelTable = new Hashtable<Integer,JLabel>();
    labelTable.put( new Integer( 0 ), new JLabel("1x") );
    labelTable.put( new Integer( -200 ), new JLabel("-2x") );
    labelTable.put( new Integer( 200 ), new JLabel("2x") );
    sliderSpeed.setLabelTable( labelTable );
    sliderSpeed.setPaintLabels(true);
}
  
  //creates a layout like the most mediaplayers are...
  private void setLayout() {
      this.setLayout(new BorderLayout());
      screenPanel=new JPanel();
      screenPanel.setLayout(new BorderLayout());
      controlsPanel=new JPanel();
      controlsPanel.setLayout(new FlowLayout());
      add(screenPanel,BorderLayout.CENTER);
      add(controlsPanel,BorderLayout.SOUTH);
      //fill screen panel
      screenPanel.add(canvasMediaPlayer,BorderLayout.CENTER);
      screenPanel.add(sliderTimeline,BorderLayout.SOUTH);
      screenPanel.add(sliderSpeed,BorderLayout.EAST);
      controlsPanel.add(btnPlay);
      controlsPanel.add(btnBack);
      controlsPanel.add(btnForward);
      controlsPanel.add(tbtnMute);
      tbtnMute.setSelected(false);
  }
  
//add UI functionality
  private void addListeners() {
      sliderTimeline.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
              if(!syncTimeline) //only if user moves the slider by hand
              {
                  if(!sliderTimeline.getValueIsAdjusting()) //and the slider is fixed
                  {
                      //recalc to 0.x percent value
                    embeddedMediaPlayer.setPosition((float)sliderTimeline.getValue()/100.0f);
                  }                   
              }
          }
          });
      
      btnPlay.addActionListener(new ActionListener() {
          
          public void actionPerformed(ActionEvent arg0) {
            System.out.println("Action: Play");
            if(embeddedMediaPlayer.isPlaying())
            {
              pause();
            }
            else{ 
              play();
            }             
          }
      });
      
      btnBack.addActionListener(new ActionListener() {
          
          public void actionPerformed(ActionEvent arg0) {
              backward();
          }
      });
      
      btnForward.addActionListener(new ActionListener() {
          
          public void actionPerformed(ActionEvent arg0) {
              forward();
          }
      });
      
      tbtnMute.addActionListener(new ActionListener() {

          public void actionPerformed(ActionEvent arg0) {
              mute();
          }
      });
      
      sliderSpeed.addChangeListener(new ChangeListener() {
          
          public void stateChanged(ChangeEvent arg0) {
              if(!sliderSpeed.getValueIsAdjusting()&&(embeddedMediaPlayer.isPlaying()))
              {
                  int perc = sliderSpeed.getValue();
                  float ratio= (float) (perc/400f*1.75);
                  ratio=ratio+(9/8);
                  embeddedMediaPlayer.setRate(ratio);
              }
              
          }
      });
      
  }
  
  protected void mute() {
    embeddedMediaPlayer.mute();

  }

  public void forward() {
    embeddedMediaPlayer.setTime((long) (embeddedMediaPlayer.getTime()+jumpLength));
  }

  public void backward() {
    embeddedMediaPlayer.setTime((long) (embeddedMediaPlayer.getTime()-jumpLength));

  }

  public void setMrl(File f)
  {
    this.mrl = f.getAbsoluteFile().toString();
  }
  
  public void setMrl(String theMrl)
  {
    this.mrl = theMrl;
  }
  
  public void play()
  {
//    embeddedMediaPlayer.parseMedia();
//    System.out.println("Duration: " + embeddedMediaPlayer.getMediaMeta().getLength());
    System.out.println("Playing video: " + mrl);
    embeddedMediaPlayer.play();
  }
  
  public void openMedia(String mrl)
  {
    String[] options = {""};
    openMedia(mrl, options);
  }
  
  public boolean openMedia(String mrl, String[] theMediaOptions)
  {
    System.out.println("Opening video: " + mrl);
    setMrl(mrl);
    embeddedMediaPlayer.setStandardMediaOptions(theMediaOptions);
    return embeddedMediaPlayer.prepareMedia(mrl, mediaOptions);
  }
  
  public void pause()
  {
    System.out.println("Pausing video: " + mrl);
    embeddedMediaPlayer.pause();
  }  

  public void removeVideo() {
    if (embeddedMediaPlayer.isPlaying())
    {
      embeddedMediaPlayer.stop();
    }
    embeddedMediaPlayer.release();
  }
  
  public void remove()
  {
    mediaPlayerFactory.release();
    embeddedMediaPlayer.release();
    this.removeAll();
  }
  
  public EmbeddedMediaPlayer getEmbeddedMediaPlayer()
  {
    return this.embeddedMediaPlayer;
  }
}
