# Use the official PostgreSQL image
FROM postgres:15

# Install build dependencies
RUN apt-get update && \
    apt-get install -y build-essential git postgresql-server-dev-15

# Clone and compile pgvector
RUN git clone https://github.com/pgvector/pgvector.git /tmp/pgvector && \
    cd /tmp/pgvector && \
    make && \
    make install

# Clean up build dependencies
RUN apt-get remove -y build-essential git postgresql-server-dev-15 && \
    apt-get autoremove -y && \
    rm -rf /var/lib/apt/lists/* /tmp/pgvector