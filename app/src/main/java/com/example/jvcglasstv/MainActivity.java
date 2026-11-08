package com.example.jvcglasstv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    private LinearLayout root, appGrid;
    private TextView clock;
    private AudioManager audio;
    private final int white = Color.WHITE;
    private final int muted = Color.rgb(185, 202, 222);
    private final int accent = Color.rgb(139, 203, 255);

    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        audio = (AudioManager)getSystemService(AUDIO_SERVICE);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        build();
    }

    private GradientDrawable bg(int color, int stroke) {
        GradientDrawable d = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{color, Color.argb(190, 23, 43, 66)});
        d.setCornerRadius(26);
        d.setStroke(2, stroke);
        return d;
    }
    private TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setGravity(Gravity.CENTER_VERTICAL); return t;
    }
    private LinearLayout row() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL); return l;
    }
    private void pad(View v, int a) { v.setPadding(a,a,a,a); }
    private void build() {
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(34,20,34,20);
        root.setBackground(new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.rgb(7,17,31), Color.rgb(17,38,61), Color.rgb(5,13,25)}));
        setContentView(root);

        LinearLayout top = row();
        TextView brand = text("JVC  |  GLASS", 19, white, true);
        top.addView(brand, new LinearLayout.LayoutParams(0, 56, 1));
        TextView profile = text("◉  Perfil", 16, muted, true); top.addView(profile);
        clock = text("  00:00  ", 18, white, true); top.addView(clock);
        Button control = smallButton("Centro de control"); control.setOnClickListener(v -> controlCenter()); top.addView(control);
        Button inputs = smallButton("Entradas"); inputs.setOnClickListener(v -> inputsDialog()); top.addView(inputs);
        root.addView(top);
        updateClock();

        LinearLayout hero = new LinearLayout(this); hero.setOrientation(LinearLayout.VERTICAL);
        hero.setPadding(28,22,28,22); hero.setBackground(bg(Color.argb(115,42,72,105), Color.argb(100,160,205,255)));
        TextView eyebrow = text("TU ENTRETENIMIENTO, EN UN SOLO LUGAR", 13, accent, true);
        TextView title = text("Bienvenido a tu TV", 30, white, true);
        TextView sub = text("Tus aplicaciones favoritas con una pantalla moderna y sencilla.", 16, muted, false);
        hero.addView(eyebrow); hero.addView(title); hero.addView(sub);
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, 130); hp.setMargins(0,16,0,14);
        root.addView(hero, hp);

        LinearLayout nav = row();
        TextView label = text("Mis aplicaciones", 22, white, true);
        nav.addView(label, new LinearLayout.LayoutParams(0,48,1));
        Button settings = smallButton("Ajustes"); settings.setOnClickListener(v -> openSettings()); nav.addView(settings);
        Button original = smallButton("Volver al Android TV original");
        original.setOnClickListener(v -> openOriginal()); nav.addView(original);
        root.addView(nav);

        ScrollView scroll = new ScrollView(this);
        appGrid = new LinearLayout(this); appGrid.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(appGrid); root.addView(scroll, new LinearLayout.LayoutParams(-1,0,1));
        loadApps();
        TextView foot = text("Navega con las flechas y OK del control remoto  •  Inicio original conservado", 12, muted, false);
        root.addView(foot);
    }

    private Button smallButton(String s) {
        Button b = new Button(this); b.setText(s); b.setTextSize(13); b.setTextColor(white);
        b.setAllCaps(false); b.setBackground(bg(Color.argb(115,38,60,85), Color.argb(100,145,190,235)));
        b.setFocusable(true); return b;
    }
    private void loadApps() {
        PackageManager pm = getPackageManager();
        Intent query = new Intent(Intent.ACTION_MAIN, null); query.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(query, 0);
        if (apps.isEmpty()) { query.removeCategory(Intent.CATEGORY_LEANBACK_LAUNCHER); query.addCategory(Intent.CATEGORY_LAUNCHER); apps = pm.queryIntentActivities(query,0); }
        Collections.sort(apps, (a,b) -> String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(), b.loadLabel(pm).toString()));
        int columns = 5;
        for (int i=0; i<apps.size(); i+=columns) {
            LinearLayout line = row(); line.setGravity(Gravity.TOP);
            for (int j=i; j<Math.min(i+columns,apps.size()); j++) {
                ResolveInfo ri = apps.get(j);
                LinearLayout tile = new LinearLayout(this); tile.setOrientation(LinearLayout.VERTICAL);
                tile.setGravity(Gravity.CENTER); tile.setPadding(8,8,8,8);
                tile.setBackground(bg(Color.argb(95,40,61,83), Color.argb(80,150,195,240)));
                ImageView icon = new ImageView(this); icon.setImageDrawable(ri.loadIcon(pm));
                tile.addView(icon, new LinearLayout.LayoutParams(54,54));
                TextView name = text(ri.loadLabel(pm).toString(), 13, white, true); name.setGravity(Gravity.CENTER);
                tile.addView(name);
                String pkg = ri.activityInfo.packageName;
                tile.setOnClickListener(v -> launch(pkg));
                tile.setFocusable(true);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,108,1); p.setMargins(5,5,5,5); line.addView(tile,p);
            }
            appGrid.addView(line);
        }
        if (apps.isEmpty()) appGrid.addView(text("No se encontraron aplicaciones visibles.",16,muted,false));
    }
    private void launch(String pkg) {
        try { Intent i = getPackageManager().getLaunchIntentForPackage(pkg); if (i != null) startActivity(i); }
        catch(Exception e) { toast("No se pudo abrir esta aplicación."); }
    }
    private void updateClock() {
        if (clock != null) clock.setText("  " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()) + "  ");
        clock.postDelayed(this::updateClock, 30000);
    }
    private void openSettings() {
        try { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
        catch(Exception e) { toast("No se pudieron abrir los ajustes."); }
    }
    private void openOriginal() {
        try {
            Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.tvlauncher");
            if (i != null) startActivity(i); else startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
        } catch(Exception e) { startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)); }
    }
    private void inputsDialog() {
        String[] opts = {"HDMI 1","HDMI 2","HDMI 3","Compuesto / AV","Inicio de Android TV"};
        new AlertDialog.Builder(this).setTitle("Entradas del televisor")
            .setItems(opts, (d,w) -> {
                if (w==4) openOriginal();
                else new AlertDialog.Builder(this).setTitle(opts[w])
                    .setMessage("El cambio directo de HDMI/AV depende del firmware de JVC. Android no ofrece un comando universal; esta versión no cambiará la entrada para evitar acciones falsas.")
                    .setPositiveButton("Entendido", null).show();
            }).setNegativeButton("Cerrar",null).show();
    }
    private void controlCenter() {
        LinearLayout box = new LinearLayout(this); box.setPadding(18,8,18,8); box.setOrientation(LinearLayout.VERTICAL);
        Button wifi = smallButton("Abrir Wi-Fi"); wifi.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS))); box.addView(wifi);
        Button bt = smallButton("Abrir Bluetooth"); bt.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS))); box.addView(bt);
        TextView vol = text("Volumen",16,white,true); box.addView(vol);
        SeekBar seek = new SeekBar(this); seek.setMax(audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seek.setProgress(audio.getStreamVolume(AudioManager.STREAM_MUSIC));
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s,int p,boolean u){ if(u) audio.setStreamVolume(AudioManager.STREAM_MUSIC,p,0); }
            public void onStartTrackingTouch(SeekBar s){} public void onStopTrackingTouch(SeekBar s){}
        }); box.addView(seek);
        new AlertDialog.Builder(this).setTitle("Centro de control").setView(box).setPositiveButton("Listo",null).show();
    }
    private void toast(String s) { Toast.makeText(this,s,Toast.LENGTH_SHORT).show(); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_VOLUME_UP || keyCode==KeyEvent.KEYCODE_VOLUME_DOWN) return super.onKeyDown(keyCode,event);
        return super.onKeyDown(keyCode,event);
    }
}
