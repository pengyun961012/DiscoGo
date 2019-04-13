# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from django.http import JsonResponse, HttpResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
from django.db import connection
import json

# Return user information.
# This is for user profile page.
# Input: http:hostname/profile/{user_id}
# return json is like:
# {
#     u_id: 1,
#     username: llw,
#     img_id: 1,
#     token: 100,
#     level: 1 
# }
def getuser(request, user_id):
    if request.method != 'GET':
        return HttpResponse(status=404)
    cursor = connection.cursor()
    cursor.execute('SELECT * FROM Users WHERE u_id = ' + str(user_id) + ';')
    return_data = cursor.fetchone()
    result = {}
    result['u_id'] = return_data[0]
    result['username'] = return_data[1]
    result['img_id'] = return_data[2]
    result['token'] = return_data[3]
    result['level'] = return_data[4]
    return JsonResponse(result)

# Return user friends information.
# This is for user profile page.
# Input http:hostname/profile/friends/1
# return json is like:
# {
#     'friends':
#     [
#         {
#             'u_id': 2,
#             'username': 'jhe',
#             'img_id': 5,
#             'best_song': 'love story'
#         },
#         {
#             'u_id': 3,
#             'username': 'cmm',
#             'img_id': 8,
#             'best_song': 'in the name of father'
#         }
#     ]
# }
def getfriends(request, user_id):
    if request.method != 'GET':
        return HttpResponse(status=404)

    result = {'friends': []}
    cursor1 = connection.cursor()
    cursor1.execute('SELECT * FROM friends WHERE u1_id = ' + str(user_id) + ';')
    friends1 = cursor1.fetchall()
    friends1 = [i[1] for i in friends1]
    cursor2 = connection.cursor()
    cursor2.execute('SELECT * FROM friends WHERE u2_id = ' + str(user_id) + ';')
    friends2 = cursor2.fetchall()
    friends2 = [i[0] for i in friends2]
    friends = friends1 + friends2


    for f_id in friends:
        friend_info = {}
        cursor3 = connection.cursor()
        cursor3.execute('SELECT * FROM users WHERE u_id = ' + str(f_id) + ';')
        f_info = cursor3.fetchone()


        friend_info['username'] = f_info[1]
        friend_info['img_id'] = f_info[3]
        cursor4 = connection.cursor()
        cursor4.execute('SELECT album_name FROM songs ' +
                        'WHERE score = ( SELECT max(score) FROM songs )')
        f_song_info = cursor4.fetchone()

        friend_info['best_song'] = f_song_info[0]
        result['friends'].append(friend_info)

    return JsonResponse(result)


# Return leaderboard information.
# This is for leaderboard page.
# Input: http:hostname/leaderboard/{user_id}
# return json is like:
# {
#   leaderboard:
#   [{
#       rank: 1,
#       img_id: 1,
#       username: llw,
#       token: 10000,
#       if_friend: true
#   }]
# }
def getleaderboard(request, user_id):
    if request.method != 'GET':
        return HttpResponse(status=404)
    cursor = connection.cursor()
    cursor.execute('SELECT * FROM Users ORDER BY token DESC;')
    return_data = cursor.fetchall()
    result = {}
    row = []
    rank = 0
    for item in return_data:
        rank += 1
        the_row = {}
        the_row['rank'] = rank
        the_row['img_id'] = item[3]
        the_row['username'] = item[1]
        the_row['token'] = item[2]
        the_id = item[0]
        cursor2 = connection.cursor()
        if the_id < int(user_id):
            the_string = 'SELECT * FROM Friends WHERE u1_id = ' + str(the_id) + ' AND u2_id = ' + str(user_id) + ' ;'
            cursor2.execute(the_string)
        else:
            the_string = 'SELECT * FROM Friends WHERE u1_id = ' + str(user_id) + ' AND u2_id = ' + str(the_id) + ' ;'
            cursor2.execute(the_string)
        rd = cursor2.fetchall()
        if rd:
            the_row['if_friend'] = True
        else:
            the_row['if_friend'] = False
        row.append(the_row)
    result['leaderboard'] = row
    return JsonResponse(result)


@csrf_exempt
def addchatt(request):
    if request.method != 'POST':
        return HttpResponse(status=404)
    json_data = json.loads(request.body)
    username = json_data['username']
    message = json_data['message']
    cursor = connection.cursor()
    cursor.execute('INSERT INTO chatts (username, message) VALUES '
                    '(%s, %s);', (username, message))
    return JsonResponse({})


# curl -X POST --header "Content-Type: application/json" 
# --data '{"username":"qyao", "img_id": 2}'
# http://localhost:9000/adduser/
@csrf_exempt
def adduser(request):
    if request.method != 'POST':
        return HttpResponse(status=404)
    json_data = json.loads(request.body)
    username = json_data['username']
    img_id = json_data['img_id']
    cursorid = connection.cursor()
    cursorid.execute('SELECT MAX(u_id) FROM Users;')
    return_data = cursorid.fetchone()
    ID = int(return_data[0])
    ID += 1
    cursor = connection.cursor()
    toExecute = "INSERT INTO users (u_id, username, token, img_id, level) VALUES (" + str(ID) + ", '" + str(username) + "', " + str(0) + ", " + str(img_id) + ", 1);"
    cursor.execute(toExecute)
    return JsonResponse({})


# curl -X POST --header "Content-Type: application/json
# --data '{"u_id":4, "username":"yqy"}'
# http://localhost:9000/updatename/
@csrf_exempt
def updatename(request):
    if request.method != 'POST':
        return HttpResponse(status=404)
    json_data = json.loads(request.body)
    u_id = json_data['u_id']
    username = json_data['username']
    cursor = connection.cursor()
    toExecute = "UPDATE users SET username = '" + str(username) + "' WHERE u_id = " + str(u_id) + ";"
    cursor.execute(toExecute)
    return JsonResponse({})


# curl -X POST --header "Content-Type: application/json
# --data '{"u1_id": 3, "u2_id": 4}'
# http://localhost:9000/updatename/
@csrf_exempt
def addfriend(request):
    if request.method != 'POST':
        return HttpResponse(status=404)
    json_data = json.loads(request.body)
    user1 = json_data['u1_id']
    user2 = json_data['u2_id']
    if int(user1) > int(user2):
        temp = user2
        user2 = user1
        user1 = temp
    cursor = connection.cursor()
    toExecute = "INSERT INTO friends (u1_id, u2_id) VALUES (" + str(user1) + ", " + str(user2) + ");"
    cursor.execute(toExecute)
    return JsonResponse({})


# Post: ["u_id":  "token": ?, "score": ?]
@csrf_exempt
def update_all(request):
    if request.method != 'POST':
        return HttpResponse(status=404)
    json_data = json.loads(request.body)
    user_id = int(json_data['u_id'])

    print "1"

    cursor1 = connection.cursor()
    cursor1.execute(" SELECT * FROM "
                    " Users WHERE u_id = %d;"
                    (user_id, ))
    user_info = cursor1.fetchall()[0]
    past_token = int(user_info['token'])
    past_score = int(user_info['score'])

    print past_token, past_score

    new_token = int(json_data['token'])
    new_score = int(json_data['score'])

    token =  new_token + past_token
    score =  new_score + past_score
    cursor3 = connection.cursor()
    cursor3.execute(" UPDATE Users"
                    " SET token = %d, score = %d"
                    " WHERE u_id = %d;",
                    (token, score, user_id))

    print token, score
    result = {}

    print("Success!")
    return JsonResponse(result)



#Table:
#u_id, time, score, link, song_name

#Post: [ u_id: xxx sing_time: xxx score: xxx link: xxx song_name: xxx]
@csrf_exempt
def Update_Link(request):
    if request.method != 'POST':
        return HttpResponse(status=404)
    json_data = json.loads(request.body)
    u_id = int(json_data['u_id'])
    sing_time = json_data['sing_time']
    sing_time = arrow.get(sing_time, 'YYYY-MM-DD HH:mm:ss')
    sing_time = sing_time.format('YYYY-MM-DD HH:mm:ss')

    score = int(json_data['score'])
    link = json_data['link']
    song_name = json_data['song_name']

    cursor = connection.cursor()
    cursor.execute('INSERT INTO songs (u_id, sing_time, score, link, song_name) VALUES '
                    '(%d, %s, %d, %s, %s);', 
                    (u_id, sing_time, score, link, song_name))
    result = {}
    return JsonResponse(result)



# Post: [u_id: sing_time: ]
# return json:
# {'link': 'xxxx' } or { }
@csrf_exempt
def Search_song(request):
    '''
    When user press the share button for a song,
    He needs a link for this song.
    If he hasn't uploaded to the cloud before,
    return empty result
    Else return the link for this song.
    '''

    if request.method != 'GET':
        return HttpResponse(status=404)
    json_data = json.loads(request.body)
    u_id = json_data['u_id']
    songtime = json_data['sing_time']

    result = {};

    sing_time = arrow.get(songtime, 'YYYY-MM-DD HH:mm:ss')
    sing_time = sing_time.format('YYYY-MM-DD HH:mm:ss')
    cursor1 = connection.cursor()
    cursor1.execute(" SELECT * FROM "
                    " Users WHERE u_id = %d and sing_time = %s;"
                    (user_id, sing_time))
    user_info = cursor1.fetchall()
    if(!user_info):
        return JsonResponse(result)

    user_info = cursor1.fetchall()[0]
    link = user_info['link']

    result['link'] = link
    return JsonResponse(result)

