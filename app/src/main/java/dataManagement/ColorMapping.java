package dataManagement;

import com.example.myfamilymap.R;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColorMapping {
    private static ColorMapping instance;

    private final HashMap<String, Float> colorMapHsl = new HashMap<>();
    private final HashMap<String, Integer> colorMapHex = new HashMap<String, Integer>();
    private final List<Float> hslColors = new ArrayList<>();
    private final List<Integer> hexColors = new ArrayList<>();

    public static ColorMapping getInstance() {
        if(instance == null) {
            instance = new ColorMapping();
            instance.preloadColors();
        }
        return instance;
    }

    public float getHue(String key) {
        key = key.toLowerCase();
        if (colorMapHsl.containsKey(key)) {
            return colorMapHsl.get(key);
        } else {
            for (int i = 0; i < hslColors.size(); i++) {
                if(!colorMapHsl.containsValue(hslColors.get(i))) {
                    colorMapHsl.put(key, hslColors.get(i));
                    colorMapHex.put(key, hexColors.get(i));
                    return hslColors.get(i);
                }
            }
            return (float)75.0;
        }
    }

    public int getHex(String key) {
        key = key.toLowerCase();
        if (colorMapHex.containsKey(key)) {
            return colorMapHex.get(key);
        } else {
            for (int i = 0; i < hexColors.size(); i++) {
                if(!colorMapHex.containsValue(hexColors.get(i))) {
                    colorMapHsl.put(key, hslColors.get(i));
                    colorMapHex.put(key, hexColors.get(i));
                    return hexColors.get(i);
                }
            }
            return R.color.defaultLifeEvent;
        }
    }

    private void preloadColors() {
        colorMapHsl.put("birth", BitmapDescriptorFactory.HUE_AZURE);
        colorMapHsl.put("death", BitmapDescriptorFactory.HUE_RED);
        colorMapHsl.put("marriage", BitmapDescriptorFactory.HUE_YELLOW);

        colorMapHex.put("birth", R.color.azure);
        colorMapHex.put("death", R.color.red);
        colorMapHex.put("marriage", R.color.yellow);

        colorMapHex.put("life", R.color.life_lines);
        colorMapHex.put("family", R.color.family_lines);
        colorMapHex.put("spouse", R.color.spouse_lines);

        hslColors.add(BitmapDescriptorFactory.HUE_ORANGE);
        hslColors.add(BitmapDescriptorFactory.HUE_ROSE);
        hslColors.add(BitmapDescriptorFactory.HUE_CYAN);
        hslColors.add(BitmapDescriptorFactory.HUE_GREEN);
        hslColors.add(BitmapDescriptorFactory.HUE_MAGENTA);
        hslColors.add(BitmapDescriptorFactory.HUE_BLUE);

        hexColors.add(R.color.orange);
        hexColors.add(R.color.rose);
        hexColors.add(R.color.cyan);
        hexColors.add(R.color.green);
        hexColors.add(R.color.magenta);
        hexColors.add(R.color.blue);
    }
}
