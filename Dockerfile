FROM gcr.io/distroless/java17-debian12:nonroot
EXPOSE 8080
COPY ./build/native/nativeCompile/worker-java /app
ENTRYPOINT ["/app"]
