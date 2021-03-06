# Copyright 2017-2019 EPAM Systems, Inc. (https://www.epam.com/)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

class PodModel(object):
    def __init__(self):
        self.identifier = None
        self.name = None
        self.namespace = None
        self.node_name = None
        self.phase = None

    @classmethod
    def load(cls, json):
        instance = cls()
        instance.identifier = json['uid']
        instance.name = json['name']
        instance.namespace = json['namespace']
        instance.node_name = json['nodeName']
        instance.phase = json['phase']
        return instance
