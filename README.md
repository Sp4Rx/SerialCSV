
# SerialCSV
[![](https://jitpack.io/v/Sp4Rx/SerialCSV.svg)](https://jitpack.io/#Sp4Rx/SerialCSV)

Serialise your java object directly to csv files .

## Installation
**Step 1.** Add it in your **root** build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
**Step 2.** Add the dependency

	dependencies {
	        compile 'com.github.Sp4Rx:SerialCSV:1.0'
	}

## Code Example
My demo **Data** model contains:

    public String name;  
    public String hobby;  
    public String occupation;  
    public int phone;

Prepare demo data :

    List<Data> data = new ArrayList<>();  
    data.add(new Data("Suvajit Sarkar", "Gaming", "Android Developer", 987654321));  
    data.add(new Data("Sourav Sarkar", "Music", "Pro Guitarist", 987654321));

***Minimal** code for exporting:*

    new SerialCSV<Data>().export(data, Data.class);

*With **callback**:*

    new SerialCSV<Data>().setCallback(new SerialCSV.Listener() {  
    @Override  
    public void onSuccess(File file) {  
        //...
    }  
  
    @Override  
    public void onFailure(String message) {  
        //...
    }  
    }).export(data, Data.class);

*Custom **file** location:*

    new SerialCSV<Data>()  
        .setFile(new File("Path to file location"))  
        .export(data, Data.class);


## License

    MIT License

    Copyright (c) 2018 Suvajit Sarkar

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
