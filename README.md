Back to School  

The goal of this service is to create a backend for managing primary school bureaucracy. The
service should be exposed as a REST API.
The back end should support the day-to-day operations of three kinds of users: parents,
teachers, and administrators. A fourth kind of user exists in the system, that is students. However,
students do not have access to the API.
Parents should be able to:
- see/modify their personal data
- see/modify the personal data of their registered children
- see the grades obtained by their children
- see/modify appointments that they have with their children's teachers (calendar-like support for
requesting appointments)
- see the monthly payments that have been made to the school in the past
- see/pay (fake payment) upcoming scheduled payments (monthly, material, trips)
- see general/personal notifications coming from the school
Teachers should be able to:
- see/modify their personal data
- see the classrooms in which they teach, with information regarding the argument that they teach
in that class, the students that make up the class, and the complete lesson timetable for that
class
- provide grades for the students in their class
- see/modify the appointments that they have with parents
Administrators should be able to:
- see the students that are enrolled in each class
- create new students in the system and enroll them in classes
- create new administrator/parent/teacher accounts
- issue new payment requests to parents
- send general notifications to all the parents/teachers in the school, to the parents/teachers of a
specific class, or to a single specific parent/teacher.
Use appropriate authentication to ensure that specific users can only interact with specific
resources.
Evaluated on: design of the API (resources/URIs/representations) + hypermedia

[Link to documentation]https://github.com/marckw94/HS/blob/master/DocumentazioneRestAPI.pdf
