Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.network "forwarded_port", guest: 8080, host: 9000
  config.vm.provider "virtualbox" do |vb|
     vb.memory = "1024"
  end
  config.vm.provision "shell", inline: <<-SHELL
    apt-get update -y
    apt-get install -y curl unzip maven --force-yes
    cd /vagrant/
    mvn spring-boot:run
  SHELL
end
