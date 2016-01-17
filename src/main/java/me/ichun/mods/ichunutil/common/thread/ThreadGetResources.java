package me.ichun.mods.ichunutil.common.thread;

import com.google.gson.Gson;
import me.ichun.mods.ichunutil.common.iChunUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class ThreadGetResources extends Thread
{
    private static final String patronList  = "https://raw.github.com/iChun/iChunUtil/master/src/main/resources/assets/ichunutil/mod/patrons.json";
    private static final String versionList = "https://raw.github.com/iChun/iChunUtil/master/src/main/resources/assets/ichunutil/mod/versions.json";
    private final Side side;

    public ThreadGetResources(Side side)
    {
        this.setName("iChunUtil Online Resource Thread");
        this.setDaemon(true);
        this.side = side;
    }

    @Override
    public void run()
    {
        //Check to see if the current client is a patron.
        if(side.isClient())
        {
            try
            {
                Gson gson = new Gson();
                Reader fileIn = new InputStreamReader(new URL(patronList).openStream());
                String[] json = gson.fromJson(fileIn, String[].class);
                fileIn.close();

                if(json != null)
                {
                    for(String s : json)
                    {
                        if(s.replaceAll("-", "").equalsIgnoreCase(iChunUtil.proxy.getPlayerId()))
                        {
                            iChunUtil.userIsPatron = true;
                            iChunUtil.config.reveal("showPatronReward", "patronRewardType");
                        }
                    }
                }
            }
            catch(Exception e)
            {
                iChunUtil.LOGGER.warn("Error retrieving iChunUtil patron list.");
                e.printStackTrace();
            }
        }
    }
}