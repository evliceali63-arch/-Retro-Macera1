package com.retromacera.game;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;

public class MainActivity extends Activity {
  @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().getDecorView().setSystemUiVisibility(5894|1024|512);setContentView(new Game());}
  class Game extends View {
    Paint p=new Paint(); float x=80,y=0,vy=0,camera=0; boolean left=false,right=false,ground=false,won=false,dead=false; int coins=0,lives=3; boolean[] collected=new boolean[12]; long last=System.nanoTime();
    final float W=960,H=540,FLOOR=425;
    Game(){super(MainActivity.this);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
    void rect(Canvas c,int color,float a,float b,float d,float e){p.setColor(color);c.drawRect(a,b,d,e,p);}
    void text(Canvas c,String s,int color,float size,float a,float b){p.setColor(color);p.setTextSize(size);p.setTypeface(android.graphics.Typeface.create("monospace",1));c.drawText(s,a,b,p);}
    void reset(){x=80;y=FLOOR-55;vy=0;camera=0;coins=0;lives=3;won=false;dead=false;collected=new boolean[12];}
    void hit(){lives--;x=Math.max(80,x-180);y=FLOOR-55;vy=0;if(lives<=0)dead=true;}
    @Override protected void onDraw(Canvas actual){super.onDraw(actual);Canvas c=actual;int save=c.save();c.scale(getWidth()/W,getHeight()/H);
      long now=System.nanoTime();float dt=Math.min(.035f,(now-last)/1e9f);last=now;
      if(!dead&&!won){if(left)x-=240*dt;if(right)x+=240*dt;x=Math.max(10,Math.min(2830,x));vy+=1000*dt;y+=vy*dt;ground=false;
        if(y+55>=FLOOR && !(x>620&&x<800) && !(x>1510&&x<1710)){y=FLOOR-55;vy=0;ground=true;}
        for(int i=0;i<12;i++){float cx=240+i*205;if(!collected[i]&&Math.abs(x-cx)<40&&Math.abs(y-(FLOOR-120))<65){collected[i]=true;coins++;}}
        if(y>H+80)hit();
        for(int i=0;i<7;i++){float hx=480+i*320;if(Math.abs(x-hx)<31&&y+55>FLOOR-37&&y<FLOOR)hit();}
        if(x>2750)won=true;
      }
      camera=Math.max(0,Math.min(2050,x-250));
      c.drawColor(Color.rgb(109,203,244));
      for(int i=0;i<10;i++){float mx=i*350-camera*.25f;rect(c,0xffa2d7a1,mx,270,mx+180,FLOOR);}
      for(int i=0;i<12;i++){float tx=i*260-camera*.5f;rect(c,0xff77503b,tx+42,295,tx+64,FLOOR);rect(c,0xff348947,tx,265,tx+110,320);}
      c.save();c.translate(-camera,0);
      rect(c,0xff70492e,0,FLOOR,620,H);rect(c,0xff70492e,800,FLOOR,1510,H);rect(c,0xff70492e,1710,FLOOR,3000,H);
      rect(c,0xff47a644,0,FLOOR,620,FLOOR+15);rect(c,0xff47a644,800,FLOOR,1510,FLOOR+15);rect(c,0xff47a644,1710,FLOOR,3000,FLOOR+15);
      for(int i=0;i<12;i++){float cx=240+i*205;if(!collected[i]){p.setColor(0xffffce39);c.drawCircle(cx,FLOOR-120,16,p);p.setColor(0xfffff1a0);c.drawCircle(cx-4,FLOOR-125,5,p);}}
      for(int i=0;i<7;i++){float hx=480+i*320;rect(c,0xffb9333b,hx-20,FLOOR-28,hx+20,FLOOR);p.setColor(0xffffa85b);android.graphics.Path path=new android.graphics.Path();path.moveTo(hx-20,FLOOR-28);path.lineTo(hx,FLOOR-60);path.lineTo(hx+20,FLOOR-28);path.close();c.drawPath(path,p);}
      rect(c,0xff292b4c,2810,245,2820,FLOOR);rect(c,0xffffdf55,2820,245,2890,285);
      rect(c,0xff263e8a,x,y+10,x+34,y+55);rect(c,0xffffd09a,x+3,y-12,x+31,y+16);rect(c,0xffe84444,x-2,y-18,x+34,y-7);rect(c,0xff101010,x+23,y-2,x+28,y+3);
      c.restore();
      rect(c,0x990b2237,0,0,W,58);text(c,"RETRO MACERA",Color.WHITE,27,22,38);text(c,"ALTIN: "+coins+"/12",0xffffe46c,25,390,38);text(c,"CAN: "+lives,0xffff8080,25,720,38);
      rect(c,0x990b2237,20,420,125,520);rect(c,0x990b2237,145,420,250,520);rect(c,0x990b2237,830,420,940,520);text(c,"<",Color.WHITE,68,46,497);text(c,">",Color.WHITE,68,170,497);text(c,"^",Color.WHITE,68,852,497);
      if(dead||won){rect(c,0xdd142036,180,150,780,360);text(c,won?"BOLUM TAMAMLANDI!":"OYUN BITTI",Color.WHITE,40,245,235);text(c,"Yeniden baslamak icin dokun",0xffffe46c,24,250,300);}
      c.restoreToCount(save);postInvalidateDelayed(16);
    }
    @Override public boolean onTouchEvent(MotionEvent e){int action=e.getActionMasked();int index=e.getActionIndex();if(action==MotionEvent.ACTION_DOWN||action==MotionEvent.ACTION_POINTER_DOWN){float px=e.getX(index)*W/getWidth(),py=e.getY(index)*H/getHeight();if(dead||won){reset();return true;}if(py>395){if(px<140)left=true;else if(px<275)right=true;else if(px>780&&ground){vy=-530;ground=false;}}}else if(action==MotionEvent.ACTION_UP||action==MotionEvent.ACTION_CANCEL){left=false;right=false;}else if(action==MotionEvent.ACTION_POINTER_UP){float px=e.getX(index)*W/getWidth();if(px<140)left=false;else if(px<275)right=false;}return true;}
  }
}
