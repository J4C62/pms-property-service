FROM mcr.microsoft.com/devcontainers/base:debian

RUN apt-get update && apt-get install -y wget gnupg xz-utils curl

# Install Java 23 from Azul
RUN curl -sL https://repos.azul.com/azul-repo.key | gpg --dearmor -o /usr/share/keyrings/azul.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/azul.gpg] https://repos.azul.com/zulu/deb stable main" > /etc/apt/sources.list.d/zulu.list && \
    apt-get update && \
    apt-get install -y zulu23-jdk

RUN java -version
RUN groupadd docker && usermod -aG docker vscode
