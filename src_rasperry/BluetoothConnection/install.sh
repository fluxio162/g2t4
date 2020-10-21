#!/bin/sh

sudo apt-get update

sudo apt-get upgrade

sudo apt-get install git

sudo apt-get install cmake

sudo apt-get purge openjdk*

sudo apt-get install openjdk-8-jdk

echo "\nexport JAVA_HOME=/usr/lib/jvm/java-8-openjdk-armhf/" >> ~/.bashrc

sudo apt-get install maven

sudo apt-get install libglib2.0-dev libdbus-1-dev libudev-dev libical-dev libreadline6 libreadline6-dev
 
sudo wget http://www.kernel.org/pub/linux/bluetooth/bluez-5.47.tar.xz

sudo tar -xf bluez-5.47.tar.xz

cd bluez-5.47

sudo ./configure --prefix=/usr --mandir=/usr/share/man --sysconfdir=/etc -- localstatedir=/var

sudo make

sudo make install

sudo sed -i '27i\\
  <policy group="bluetooth">\\
    <allow send_destination="org.bluez"/>\\
  </policy>\\
' /etc/dbus-1/system.d/bluetooth.conf 

sudo adduser --system --no-create-home --group --disabled-login openhab

sudo usermod -a -G bluetooth openhab

sudo systemctl daemon-reload

sudo systemctl restart bluetooth

sudo apt-get install graphviz

sudo apt-get install doxygen

cd ..

sudo git clone https://github.com/intel-iot-devkit/tinyb.git 

cd tinyb

sudo mkdir build

cd build

sudo -E cmake -DBUILDJAVA=ON -DCMAKE_INSTALL_PREFIX=/usr ..

sudo make

sudo make install

cd
exit

