package Test20171115;

import java.util.Arrays;

public class RGB_HSB {
	public static void main(String[] args) {
		int[] rgb = toRGB("#ffffff");
		float[] hsb = RGBToHVB(rgb);
		for (int i = 0; i < hsb.length; i++) {
			// HSV(Hue色相、Saturation饱和度、Value(Brightness)明度，也叫HSB)
			System.out.println(hsb[i]);
		}
		System.out.println(isLightColor(rgb));
	}

	public static int[] toRGB(String str) {
		int[] rgb = { -1, -1, -1 };
		if (!(str.length() == 7)) {
			System.err.println("格式不正确！必须为六位十六进制数");
		} else {
			String str2 = str.substring(1, 3);
			String str3 = str.substring(3, 5);
			String str4 = str.substring(5, 7);
			int red = Integer.parseInt(str2, 16);
			int green = Integer.parseInt(str3, 16);
			int blue = Integer.parseInt(str4, 16);
			rgb[0] = red;
			rgb[1] = green;
			rgb[2] = blue;
		}
		return rgb;
	}

	public static float[] RGBToHVB(int[] rgb) {
		if (rgb.length == 3) {
			int rgbR, rgbG, rgbB;
			rgbR = rgb[0];
			rgbG = rgb[1];
			rgbB = rgb[2];
			assert 0 <= rgbR && rgbR <= 255;
			assert 0 <= rgbG && rgbG <= 255;
			assert 0 <= rgbB && rgbB <= 255;
			Arrays.sort(rgb);
			int max = rgb[2];
			int min = rgb[0];

			float hsbB = max / 255.0f;
			float hsbS = max == 0 ? 0 : (max - min) / (float) max;

			float hsbH = 0;
			if (max == rgbR && rgbG >= rgbB) {
				hsbH = (rgbG - rgbB) * 60f / (max - min) + 0;
			} else if (max == rgbR && rgbG < rgbB) {
				hsbH = (rgbG - rgbB) * 60f / (max - min) + 360;
			} else if (max == rgbG) {
				hsbH = (rgbB - rgbR) * 60f / (max - min) + 120;
			} else if (max == rgbB) {
				hsbH = (rgbR - rgbG) * 60f / (max - min) + 240;
			}

			return new float[] { hsbH, hsbS, hsbB };
		}
		return null;
	}

	public static int[] HBSToRGB(float[] hsv) {
		float h, s, v;
		h = hsv[0];
		s = hsv[1];
		v = hsv[2];
		assert Float.compare(h, 0.0f) >= 0 && Float.compare(h, 360.0f) <= 0;
		assert Float.compare(s, 0.0f) >= 0 && Float.compare(s, 1.0f) <= 0;
		assert Float.compare(v, 0.0f) >= 0 && Float.compare(v, 1.0f) <= 0;

		float r = 0, g = 0, b = 0;
		int i = (int) ((h / 60) % 6);
		float f = (h / 60) - i;
		float p = v * (1 - s);
		float q = v * (1 - f * s);
		float t = v * (1 - (1 - f) * s);
		switch (i) {
		case 0:
			r = v;
			g = t;
			b = p;
			break;
		case 1:
			r = q;
			g = v;
			b = p;
			break;
		case 2:
			r = p;
			g = v;
			b = t;
			break;
		case 3:
			r = p;
			g = q;
			b = v;
			break;
		case 4:
			r = t;
			g = p;
			b = v;
			break;
		case 5:
			r = v;
			g = p;
			b = q;
			break;
		default:
			break;
		}
		return new int[] { (int) (r * 255.0), (int) (g * 255.0),
				(int) (b * 255.0) };
	}

	public static boolean isLightColor(int[] rgb) {
		if (rgb.length == 3) {
			int R = rgb[0];
			int G = rgb[1];
			int B = rgb[2];
			double grayLevel = R * 0.299 + G * 0.587 + B * 0.114;
			return grayLevel >= 192 ? true : false;
		} else {
			return false;
		}
	}
}
