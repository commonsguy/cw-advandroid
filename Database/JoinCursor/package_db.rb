=begin
	Copyright (c) 2008 -- CommonsWare, LLC

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
=end
	 
require 'rubygems'
require 'sqlite3'

Dir['db/*'].each do |path|
	db=SQLite3::Database.new(path)
	
	begin
		db.execute("SELECT name FROM sqlite_master WHERE type='table'") do |row|
			if ARGV.include?(row[0])
				puts `sqlite3 #{path} ".dump #{row[0]}"`
			end
		end
	ensure
		db.close
	end
end
