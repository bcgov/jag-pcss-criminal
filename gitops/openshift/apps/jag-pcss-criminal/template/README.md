## Templates to create openshift components related to jag-pcss-criminal api deployment

### Command to execute template
1) Login to OC using login command
2) Run below command in each env. namespace dev/test/prod/tools
   ``oc process -f jag-pcss-criminal.yaml --param-file=jag-pcss-criminal.env | oc apply -f -``


