package ie.tudublin;

import processing.core.PApplet;



public class Arrays extends PApplet
{


	String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

	float[] rainfall = {200, 260, 300, 150, 100, 50, 10, 40, 67, 160, 400, 420};

	int mode = 0;

	int minIndex = 0;
	int maxIndex = 0;

	public void keyPressed()
	{
		if(key >= '0' && key <= '9')
		{
			mode = key - '0';
		}//end if
		println(mode);
	}

	public float map1(float a, float b, float c, float d, float e)
	{
		float r1 = c -b;
		float r2 = e - d;

		float howFar = a - b;
		return d + (howFar / r1) * r2;
	}

	void randomize()
	{
		for (int i = 0; i < rainfall.length; i++) {
			rainfall[i] = random(500);
		}
	}

	public void settings()
	{
		size(500, 500);

		String[] m1 = months;
		print(m1[0]);
		for(int i = 0; i < months.length; i ++)
		{
			println("Month: " + months[i] + "\t" + rainfall[i]);
		}
		for (String s : m1) {
			println(s);
		}

		int minIndex = 0;
		for(int i= 0 ; i < rainfall.length ; i ++)
		{
			if (rainfall[i] < rainfall[minIndex])
			{
				minIndex = i;
			}
		}
		
		int maxIndex = 0;
		for(int i= 0 ; i < rainfall.length ; i ++)
		{
			if (rainfall[i] > rainfall[maxIndex])
			{
				maxIndex = i;
			}
		}

		println("The month with the minimum rainfall was " + months[minIndex] + " with " + rainfall[minIndex] + "rain");
		println("The month with the max rainfall was " + months[maxIndex] + " with " + rainfall[maxIndex] + "rain");
		
		
		float tot = 0;
		for(float f:rainfall)
		{
			tot += f;
		}

		float avg = tot / (float) rainfall.length;

		// a b-c d-e;
		println(map1(5, 0, 10, 0, 100));
		// 50

		println(map1(25, 20, 30, 200, 300));
		// 250

		println(map1(26, 25, 35, 0, 100));
		// 10 


	}

	public void setup() {
		colorMode(HSB);
		background(255);
		//randomize();
	}

	
	public void draw()
	{	//X axis
		switch (mode) {
			case 0:
			{
				stroke(0);
				line(0, height, width, height);

				colorMode(HSB);
				background(0);
				float w = width / (float)months.length;
				float maxRainfall = max(rainfall);

				for(int i = 0 ; i < months.length ;  i ++)
				{
					float x = i * w;
					float barHeight = map1(rainfall[i] , 0, maxRainfall, 0, height);
					float y = height - barHeight;

					float hue = map1(i, 0, 16, 0, 300);

					fill(hue, 255, 255);

					rect(x, y, w, barHeight);

					textAlign(CENTER, TOP);
					fill(255);
					text(months[i], x + w / 2, height - 20);
				}
				break;
			}// end case 0

			case 1:
			{
				background(0);
				float border = width * 0.1f;
				
				//drawing axis
				stroke(255);
				line(border, border, border, height - border);
				line(border, height - border, width - border, height -border);
	
				for (int i = 0; i <= 0; i++)
				{
					float y = map(i, 0, 120, height - border, border);
					line(border - 5, y, border, y);
					fill(255);
					textAlign(CENTER, CENTER);
					text(i, border / 2, y);

				}// end for
				float w =  (width - (border * 2.0f)) / (float)rainfall.length;

				for(int i = 0; i < rainfall. length; i ++)
				{
					float x = map(i, 0, rainfall.length, border, width-border);
					float c = map(i, 0, rainfall.length, 0, 255);
					stroke(255);
					fill(c, 255, 255);
					float h = map(rainfall[i], 0, 120, 0, -height + (border * 2.0f)); 
					rect(x, height-border, w, h);
					fill(255);
					text(months[i], x + (w / 2), height - (border / 2));
					
				}// end for

				for (int i = 0; i <= max(rainfall); i += 20)
				{
					float y = map(i, 0, max(rainfall), height - border, border);
					line(border - 5, y, border, y);
					fill(255);
					textAlign(RIGHT, CENTER); // Align text to the right of the tick
					text(i, border - 10, y); // Draw the label to the left of the tick
				}
				break;
			}// end case 1

			case 2:
			{
				background(0);
				float step = width / (float)(months.length - 1);
				float xPrev = 0;
				float yPrev = map(rainfall[0], 0, max(rainfall), height, 0); // Start at the first data point
				
				// Calculate linear regression (least squares method)
				float sumX = 0;
				float sumY = 0;
				float sumXY = 0;
				float sumX2 = 0;
				for (int i = 0; i < months.length; i++)
				{
					sumX += i;
					sumY += rainfall[i];
					sumXY += i * rainfall[i];
					sumX2 += i * i;
				}
				float n = months.length;
				float slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
				float yIntercept = (sumY - slope * sumX) / n;
				
				for (int i = 0; i < months.length; i++)
				{
					float x = i * step;
					float y = map(rainfall[i], 0, max(rainfall), height, 0); // Current data point

					stroke(255);
					line(xPrev, yPrev, x, y); // Draw line segment from previous point to current point

					xPrev = x;
					yPrev = y; // Update previous point for the next iteration
					
					// Draw data points as circles
					fill(255);
					ellipse(x, y, 5, 5);
					
					// Label months on x-axis
					textAlign(CENTER);
					fill(255);
					text(months[i], x, height - 5);
				}
				
				// Draw trend line
				stroke(255, 0, 0); // Red color for trend line
				float trendLineStartX = 0;
				float trendLineStartY = map(yIntercept, 0, max(rainfall), height, 0);
				float trendLineEndX = width - 1;
				float trendLineEndY = map((slope * (months.length - 1) + yIntercept), 0, max(rainfall), height, 0);
				line(trendLineStartX, trendLineStartY, trendLineEndX, trendLineEndY);

				// Label y-axis
				textAlign(RIGHT, CENTER);
				fill(255);
				for (int i = 0; i <= max(rainfall); i += 20)
				{
					float y = map(i, 0, max(rainfall), height, 0);
					text(i, width - 5, y);
				}
				break;
			}// end case 

			case 3:
			{
				background(0);
				float r = mouseX;
				float cx = width / 2;
				float cy = height / 2;
				stroke(255);
				noFill();
				//circle(cx, cy, r * 2.0f);
				float tot = 0;
				for(float f:rainfall)
				{
					tot += f;
				}
				float start = 0;

				for(int i = 0 ; i < rainfall.length ; i ++)
				{
					float val = map(rainfall[i], 0, tot, 0, TWO_PI);
					float c = map(i, 0, rainfall.length, 0, 255);
					noStroke();
					fill(c, 255, 255);
					arc(cx, cy, r * 2, r * 2, start, start + val, PIE);
						
					float theta = start + (val * 0.5f);
					float x = cx + cos(theta) * (r * 1.2f);
					float y = cy + sin(theta) * (r * 1.2f);
					fill(255);
					text(months[i], x, y);
					start = start + val;
				}
				break;
			}// end case 3
				
		}// end switch
		
	}
}
