# Intelligent Scheduler  
# Buddh

Why **Buddh** as project name?  
Buddh means the enlightened one.  
This project guide us to provide the correct solution for different constraint of our life so as to reach desired state of Nirvana.

## Data Model  

Qualified Patient KPI
- Patient ID, Measure Date, Care Gap 

Patient Details
- Patient ID, City

Health Coaches Availability
- HC ID, Date, Start Time, End Time

Health Coach Location
- HC ID, City

Health Coaches Expertise
- HC ID, Care Gap List

## Hard Constraint  
- One Patient will coordinate with one HC during care coordination call.
- One call will take 10 min.
- KPI should reach to 90%.

## Soft Constraint
Same HC for further call follow up.
Same City.

## Future Scope
- HC Rating
- Patient Health Improvement